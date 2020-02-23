package spring.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import spring.scheduler.entity.EmailMessage;
import spring.scheduler.exception.ExceptionKind;
import spring.scheduler.exception.ExtendedRuntimeException;
import spring.scheduler.repository.EmailMessageRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmailMessageService {
    private EmailMessageRepository emailMessageRepository;
    private JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String emailFrom;

    @Autowired
    public EmailMessageService(EmailMessageRepository emailMessageRepository, JavaMailSender mailSender) {
        this.emailMessageRepository = emailMessageRepository;
        this.mailSender = mailSender;
    }

    public Optional<EmailMessage> findEmailMessageByEmail(String email) {
        try {
            return emailMessageRepository.findByEmail(email);
        } catch (Exception e) {
            log.error("exception during findByEmail: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    Optional<EmailMessage> findEmailMessageByQuickAccessCode(String quickAccessCode) {
        try {
            return emailMessageRepository.findEmailMessageByQuickAccessCode(quickAccessCode);
        } catch (Exception e) {
            log.error("exception during findEmailMessageByQuickAccessCode: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    void addEmailToQueue(String email, String subject, String textMessage, String quickAccessCode) {
        EmailMessage emailMessage = EmailMessage.builder()
                .email(email)
                .subject(subject)
                .textMessage(textMessage)
                .quickAccessCode(quickAccessCode)
                .dateCreated(LocalDate.now())
                .build();

        try {
            emailMessageRepository.save(emailMessage);
        } catch (Exception e) {
            log.error("exception during emailMessageRepository.save: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    private void sendAnEmail(EmailMessage emailMessage) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailMessage.getEmail());
        mailMessage.setSubject(emailMessage.getSubject());
        mailMessage.setText(emailMessage.getTextMessage());

        try {
            mailSender.send(mailMessage);
        } catch (Exception e) {
            log.error("exception during mailSender.send: " + e.getMessage());
            return;
        }

        emailMessage.setSent(true);
        try {
            emailMessageRepository.save(emailMessage);
        } catch (Exception e) {
            log.error("exception during emailMessageRepository.save: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    void sendNotSentEmailsFromQueue() {
        Runnable runnable = () -> {
            List<EmailMessage> listNotSent;

            try {
                listNotSent = emailMessageRepository.findNotSent();
            } catch (Exception e) {
                log.error("exception during findNotSent: " + e.getMessage());
                throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
            }

            listNotSent.forEach(this::sendAnEmail);
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    void save(EmailMessage emailMessage) {
        try {
            emailMessageRepository.save(emailMessage);
        } catch (Exception e) {
            log.error("exception during emailMessageRepository.save: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }
}