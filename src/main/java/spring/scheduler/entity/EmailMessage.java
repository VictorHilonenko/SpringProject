package spring.scheduler.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "email_messages")
public class EmailMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String textMessage;

    @Column(nullable = false)
    private LocalDate dateCreated;

    @Column(nullable = true)
    private boolean sent;

    @Column(nullable = true)
    private String quickAccessCode;
}