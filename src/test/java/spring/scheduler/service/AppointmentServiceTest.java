package spring.scheduler.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.scheduler.dto.AppointmentDTO;
import spring.scheduler.entity.Appointment;
import spring.scheduler.entity.EmailMessage;
import spring.scheduler.entity.User;
import spring.scheduler.entity.enums.Role;
import spring.scheduler.entity.enums.ServiceType;
import spring.scheduler.exception.ExtendedRuntimeException;
import spring.scheduler.repository.AppointmentRepository;
import spring.scheduler.repository.EmailMessageRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService instance;

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private UserService userService;
    @Mock
    private EmailMessageService emailMessageService;
    @Mock
    private EmailMessageRepository emailMessageRepository;

    private Appointment appointmentSample;
    private String email;

    @Before
    public void setUp() throws Exception {
        email = "email@mail.com";

        User user = new User(1, "name", "", email, "", "", Role.ROLE_MASTER, ServiceType.NULL);

        appointmentSample = new Appointment();
        appointmentSample.setAppointmentDate(LocalDate.now());
        appointmentSample.setCustomer(user);
        appointmentSample.setMaster(user);
        appointmentSample.setServiceType(ServiceType.HAIRDRESSING);
    }

    @Test
    public void shouldFindById() {
        instance.findById(1);

        verify(appointmentRepository, times(1)).findById(1);
    }

    @Test
    public void shouldGetAllAppointmentsDTO() {
        when(userService.getCurrentUserName()).thenReturn("email@mail.com");
        when(userService.getCurrentRole()).thenReturn(Role.ROLE_ADMIN);

        instance.getAllAppointmentsDTO("2020-02-01", "2020-02-01");

        verify(appointmentRepository, times(1)).findByPeriod(anyString(), anyString());
    }

    @Test(expected = ExtendedRuntimeException.class)
    public void shouldThrowExceptionAddAppointment() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.getMap().put("date", "2020-02-01");
        appointmentDTO.getMap().put("time", "18");
        appointmentDTO.getMap().put("serviceType", "HAIRDRESSING");

        User user = new User();
        user.setEmail(email);

        when(userService.getCurrentUser()).thenReturn(Optional.of(user));
        when(appointmentRepository.reserveTime(anyString(), anyString(), anyString(), anyString())).thenReturn(1);

        instance.addAppointment(appointmentDTO);
    }

    @Test
    public void shouldUpdateServiceProvided() {
        String email = "email@mail.com";

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.getMap().put("id", "1");
        appointmentDTO.getMap().put("serviceProvided", "true");

        EmailMessage emailMessage = new EmailMessage();

        when(appointmentRepository.findById(anyInt())).thenReturn(Optional.ofNullable(appointmentSample));
        when(appointmentRepository.save(appointmentSample)).thenReturn(appointmentSample);
        when(emailMessageRepository.save(any(EmailMessage.class))).thenReturn(emailMessage);
        when(userService.getCurrentUserName()).thenReturn(email);

        instance.updateServiceProvided(appointmentDTO);

        verify(emailMessageService, times(1)).sendNotSentEmailsFromQueue();
    }
}