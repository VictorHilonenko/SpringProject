package spring.scheduler.dto;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {
    @Nullable
    private int id;

    @Size(min = 2, message = "warning.latinNotLessThen2")
    private String firstName;

    @NotEmpty(message = "warning.empty")
    private String lastName;

    @Email(message = "warning.emailWrong")
    @NotEmpty(message = "warning.empty")
    private String email;

    @Pattern(regexp = "^\\+?380\\d{9}$", message = "warning.wrongTelNumberFormat")
    private String telNumber;

    @NotEmpty(message = "warning.empty")
    private String password;

    @Nullable
    private String role;

    @Nullable
    private String serviceType;
}