package spring.scheduler.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FeedbackDTO {
    @NotNull
    private int id;

    @NotNull
    private byte rating;

    @NotNull
    private String textMessage;
}