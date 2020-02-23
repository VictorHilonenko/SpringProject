package spring.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import spring.scheduler.dto.FeedbackDTO;
import spring.scheduler.entity.Feedback;
import spring.scheduler.entity.User;
import spring.scheduler.exception.ExceptionKind;
import spring.scheduler.exception.ExtendedRuntimeException;
import spring.scheduler.repository.FeedbackRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FeedbackService {
    private FeedbackRepository feedbackRepository;
    private UserService userService;
    private AppointmentService appointmentService;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository,
                           UserService userService,
                           AppointmentService appointmentService) {

        this.feedbackRepository = feedbackRepository;
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    public void createNewFeedbacksOnProvidedServicesForCustomer(User user) {
        try {
            feedbackRepository.createNewFeedbacksOnProvidedServicesForCustomer(user.getId());
        } catch (Exception e) {
            log.error("exception during createNewFeedbacksOnProvidedServicesForCustomer: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    public List<Feedback> getFeedbacksToLeave(User user) {
        try {
            return feedbackRepository.findFeedbacksToLeave(user.getId());
        } catch (Exception e) {
            log.error("exception during findFeedbacksToLeave: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    public Page<Feedback> getCustomersFeedbacks(User user, Pageable pageable) {
        try {
            return feedbackRepository.findAllLeftByCustomerId(user.getId(), pageable);
        } catch (Exception e) {
            log.error("exception during findAllLeftByCustomerId: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    public Page<Feedback> getMastersFeedbacks(User user, Pageable pageable) {
        try {
            return feedbackRepository.findAllLeftForMasterId(user.getId(), pageable);
        } catch (Exception e) {
            log.error("exception during findAllLeftForMasterId: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    public Page<Feedback> getAllFeedbacks(User user, Pageable pageable) {
        try {
            return feedbackRepository.findAllLeft(pageable);
        } catch (Exception e) {
            log.error("exception during findAllLeft: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    public void checkFeedbackDTO(FeedbackDTO feedbackDTO, BindingResult result) {
        byte rating = Optional.ofNullable(feedbackDTO.getRating()).orElse((byte) 0);
        String strTextMessage = Optional.ofNullable(feedbackDTO.getTextMessage()).orElse("");

        if (rating == 0 || "".equals(strTextMessage)) {
            result.rejectValue("textMessage", null, "you need to fill rating and text");
        }

        if (rating < 0 || rating > 10) {
            result.rejectValue("rating", null, "wrong rating");
        }
    }

    public Feedback findAndCheckFeedback(int feedbackId) {
        User user = userService.getCurrentUser()
                .orElseThrow(() -> new ExtendedRuntimeException(ExceptionKind.ACCESS_DENIED));

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ExtendedRuntimeException(ExceptionKind.WRONG_DATA_PASSED));

        if (!feedback.getAppointment().getCustomer().equals(user)) {
            throw new ExtendedRuntimeException(ExceptionKind.ACCESS_DENIED);
        }

        byte ratingExisting = Optional.ofNullable(feedback.getRating()).orElse((byte) 0);
        String strTextMessageExisting = Optional.ofNullable(feedback.getTextMessage()).orElse("");

        if (ratingExisting > 0 || !"".equals(strTextMessageExisting)) {
            throw new ExtendedRuntimeException(ExceptionKind.NOT_ACCEPTABLE_DATA_PASSED);
        }

        return feedback;
    }

    public void updateFeedback(Feedback feedback, FeedbackDTO feedbackDTO) {
        feedback.setRating(feedbackDTO.getRating());
        feedback.setTextMessage(feedbackDTO.getTextMessage());
        try {
            feedbackRepository.save(feedback);
        } catch (Exception e) {
            log.error("exception during feedbackRepository.save: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    public void processQuickLink(String quickAccessCode, int appointmentId) {
        appointmentService.findById(appointmentId)
                .orElseThrow(() -> new ExtendedRuntimeException(ExceptionKind.PAGE_NOT_FOUND));

        try {
            userService.setAuthenticationByQuickAccessCode(quickAccessCode);
        } catch (Exception e) {
            log.error("exception during setAuthenticationByQuickAccessCode: " + e.getMessage());
        }
    }
}