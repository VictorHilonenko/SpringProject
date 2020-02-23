package spring.scheduler.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import spring.scheduler.dto.FeedbackDTO;
import spring.scheduler.entity.Feedback;
import spring.scheduler.entity.User;
import spring.scheduler.entity.enums.Role;
import spring.scheduler.exception.ExceptionKind;
import spring.scheduler.exception.ExtendedRuntimeException;
import spring.scheduler.service.FeedbackService;
import spring.scheduler.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class FeedbackController {
    private UserService userService;
    private FeedbackService feedbackService;

    @Autowired
    public FeedbackController(UserService userService, FeedbackService feedbackService) {
        this.userService = userService;
        this.feedbackService = feedbackService;
    }

    @ModelAttribute("feedback")
    public FeedbackDTO feedbackUpdateDTO() {
        return new FeedbackDTO();
    }

    @GetMapping("/feedbacks/{appointmentId}/{quickAccessCode}")
    public String openAppointmentForFeedback(@PathVariable(name = "appointmentId", required = true) int appointmentId,
                                             @PathVariable(name = "quickAccessCode", required = true) String quickAccessCode) {
        try {
            feedbackService.processQuickLink(quickAccessCode, appointmentId);
        } catch (Exception e) {
            log.error("exception during processQuickLink: " + e.getMessage());
        }

        return "redirect:/feedbacks";
    }

    @GetMapping("/feedbacks")
    public String feedbacks(@PageableDefault Pageable pageable, Model model) {
        User user = userService.getCurrentUser()
                .orElseThrow(() -> new ExtendedRuntimeException(ExceptionKind.ACCESS_DENIED));

        if (user.getRole().equals(Role.ROLE_USER)) {
            return prepareFeedbacksForUser(user, model, pageable);
        } else if (user.getRole().equals(Role.ROLE_MASTER)) {
            return prepareFeedbacksForMaster(user, model, pageable);
        } else if (user.getRole().equals(Role.ROLE_ADMIN)) {
            return prepareFeedbacksForAdmin(user, model, pageable);
        } else {
            throw new ExtendedRuntimeException(ExceptionKind.ACCESS_DENIED);
        }
    }

    private String prepareFeedbacksForUser(User user, Model model, Pageable pageable) {
        feedbackService.createNewFeedbacksOnProvidedServicesForCustomer(user);

        List<Feedback> listFeedbacksToLeave = feedbackService.getFeedbacksToLeave(user);
        model.addAttribute("listFeedbacksToLeave", listFeedbacksToLeave);

        Page<Feedback> pageCustomersFeedbacks = feedbackService.getCustomersFeedbacks(user, pageable);
        model.addAttribute("pageCustomersFeedbacks", pageCustomersFeedbacks);

        return "feedbacks_user";
    }

    private String prepareFeedbacksForMaster(User user, Model model, Pageable pageable) {
        Page<Feedback> pageAllFeedbacksForRole = feedbackService.getMastersFeedbacks(user, pageable);
        model.addAttribute("pageAllFeedbacksForRole", pageAllFeedbacksForRole);

        return "feedbacks_master";
    }

    private String prepareFeedbacksForAdmin(User user, Model model, Pageable pageable) {
        Page<Feedback> pageAllFeedbacksForRole = feedbackService.getAllFeedbacks(user, pageable);
        model.addAttribute("pageAllFeedbacksForRole", pageAllFeedbacksForRole);

        return "feedbacks_admin";
    }

    @PostMapping("/feedbacks")
    public String feedbackLeft(@Valid @ModelAttribute("feedback") FeedbackDTO feedbackDTO, BindingResult result, Model model, @PageableDefault Pageable pageable) {
        feedbackService.checkFeedbackDTO(feedbackDTO, result);

        if (result.hasErrors()) {
            return feedbacks(pageable, model);
        }

        Feedback feedback = feedbackService.findAndCheckFeedback(feedbackDTO.getId());

        try {
            feedbackService.updateFeedback(feedback, feedbackDTO);
        } catch (Exception e) {
            log.error("exception during feedback updating: " + e.getMessage());
            result.rejectValue("id", null, "Can't save your feedback...");
        }

        return feedbacks(pageable, model);
    }
}