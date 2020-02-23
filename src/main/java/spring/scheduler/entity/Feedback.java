package spring.scheduler.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(nullable = false)
    private Appointment appointment;

    @Column(nullable = false, columnDefinition = "tinyint(4) DEFAULT 0")
    private byte rating;

    @Column(nullable = false)
    private String textMessage;
}