package spring.scheduler.entity;

import lombok.*;
import spring.scheduler.entity.enums.ServiceType;

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
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private byte appointmentTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @OneToOne
    @JoinColumn(nullable = false)
    private User customer;

    @OneToOne
    @JoinColumn(nullable = false)
    private User master;

    @Column(nullable = false, columnDefinition = "bit(1) DEFAULT 0")
    private boolean serviceProvided;
}