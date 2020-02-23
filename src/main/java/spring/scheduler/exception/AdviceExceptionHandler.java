package spring.scheduler.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@ControllerAdvice
public class AdviceExceptionHandler extends ResponseEntityExceptionHandler {
    private MessageSource messageSource;

    @Autowired
    public AdviceExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ExtendedRuntimeException.class)
    protected String handleAdviceException(ExtendedRuntimeException extendedRuntimeException, Model model, HttpServletResponse response, HttpServletRequest request) {
        int status = extendedRuntimeException.getAnError().getStatusCode();
        response.setStatus(status);
        model.addAttribute("status", status);

        Locale locale = LocaleContextHolder.getLocale();

        String localizedMessage = messageSource.getMessage(extendedRuntimeException.getAnError().getErrorMessage(), null, locale);
        model.addAttribute("message", localizedMessage);

        String requestURI = request.getRequestURI();
        if (requestURI.contains("/api/")) {
            return "error_api";
        } else {
            return "error";
        }
    }
}