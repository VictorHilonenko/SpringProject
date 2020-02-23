package spring.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.scheduler.dto.AppointmentDTO;
import spring.scheduler.entity.Appointment;
import spring.scheduler.entity.User;
import spring.scheduler.entity.enums.Role;
import spring.scheduler.entity.enums.ServiceType;
import spring.scheduler.exception.ExceptionKind;
import spring.scheduler.exception.ExtendedRuntimeException;
import spring.scheduler.repository.AppointmentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AppointmentService {
    private AppointmentRepository appointmentRepository;
    private UserService userService;
    private EmailMessageService emailMessageService;
    private final String HIDDEN = "H";
    private final String READONLY = "R";
    private final String WRITE = "W";

    @Value("${app.WORK_TIME_STARTS}")
    private int WORK_TIME_STARTS;

    @Value("${app.WORK_TIME_ENDS}")
    private int WORK_TIME_ENDS;

    @Value("${app.NO_IDLE_MASTER_RESTRICTION}")
    private String strNoIdleMasterRestriction;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, UserService userService, EmailMessageService emailMessageService) {
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
        this.emailMessageService = emailMessageService;
    }

    Optional<Appointment> findById(int id) {
        try {
            return appointmentRepository.findById(id);
        } catch (Exception e) {
            log.error("exception during findById: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    public List<AppointmentDTO> getAllAppointmentsDTO(String strDateStart, String strDateEnd) {
        String email = userService.getCurrentUserName();
        Role role = userService.getCurrentRole();

        try {
            return appointmentRepository.findByPeriod(strDateStart, strDateEnd)
                    .stream()
                    .map(a -> appointmentToDTO(a, email, role))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("exception during findByPeriod: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    private boolean validateAppointmentDTOForAdd(String strDate, String strTime, String strServiceType) {
        byte time = Byte.parseByte(strTime);
        if ((time < WORK_TIME_STARTS) || (time > WORK_TIME_ENDS)) {
            return false;
        }

        try {
            strServiceType = ServiceType.valueOf(strServiceType).name();
        } catch (IllegalArgumentException e) {
            log.error("wrong ServiceType value: " + strServiceType);
            return false;
        }

        return true;
    }

    public boolean addAppointment(AppointmentDTO appointmentDTO) {
        HashMap<String, String> map = appointmentDTO.getMap();
        String date = map.get("date");
        String time = map.get("time");
        String service_type = map.get("serviceType");

        if (!validateAppointmentDTOForAdd(date, time, service_type)) {
            throw new ExtendedRuntimeException(ExceptionKind.WRONG_DATA_PASSED);
        }

        User user = userService.getCurrentUser()
                .orElseThrow(() -> new ExtendedRuntimeException(ExceptionKind.ACCESS_DENIED));

        try {
            appointmentRepository.reserveTime(user.getEmail(), date, time, service_type);
        } catch (org.springframework.dao.DataIntegrityViolationException eIntegrity) {
            if (strNoIdleMasterRestriction.equalsIgnoreCase(eIntegrity.getCause().getCause().getMessage())) {
                throw new ExtendedRuntimeException(ExceptionKind.NO_IDLE_MASTER, eIntegrity);
            } else {
                log.error("exception during appointmentRepository.reserveTime: " + eIntegrity);
                throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, eIntegrity);
            }
        } catch (Exception e) {
            log.error("exception during appointmentRepository.reserveTime: " + e);
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }

        return true;
    }

    @Transactional
    public boolean updateServiceProvided(AppointmentDTO appointmentDTO) {
        HashMap<String, String> map = appointmentDTO.getMap();

        int id;
        try {
            id = Integer.parseInt(map.get("id"));
        } catch (Exception e) {
            log.error("exception during updateServiceProvided: " + e);
            throw new ExtendedRuntimeException(ExceptionKind.WRONG_DATA_PASSED, e);
        }

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ExtendedRuntimeException(ExceptionKind.WRONG_DATA_PASSED));

        boolean serviceProvidedNewValue = "true".equals(map.get("serviceProvided"));

        if (!changesPermittedAndNeeded(appointment, serviceProvidedNewValue)) {
            throw new ExtendedRuntimeException(ExceptionKind.ACCESS_DENIED);
        }

        appointment.setServiceProvided(serviceProvidedNewValue);
        try {
            appointmentRepository.save(appointment);
        } catch (Exception e) {
            log.error("exception during appointmentRepository.save: " + e);
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }

        addFeedbackEmailToQueue(appointment);

        return true;
    }

    private boolean changesPermittedAndNeeded(Appointment appointment, boolean serviceProvidedNewValue) {
        if (!appointment.getMaster().getEmail().equals(userService.getCurrentUserName())) {
            return false;
        }

        if (!serviceProvidedNewValue) {
            return false;
        }

        return serviceProvidedNewValue != appointment.isServiceProvided();
    }

    private void addFeedbackEmailToQueue(Appointment appointment) {
        String quickAccessCode = UUID.randomUUID().toString();
        String subject = "leave feedback, please";
        String serverAddress = "http://localhost:" + serverPort;
        String message = "please, leave your feedback here: " + serverAddress + "/feedbacks/" + appointment.getId() + "/" + quickAccessCode;
        String email = appointment.getCustomer().getEmail();

        emailMessageService.addEmailToQueue(email, subject, message, quickAccessCode);

        emailMessageService.sendNotSentEmailsFromQueue();
    }

    private AppointmentDTO appointmentToDTO(Appointment appointment, String email, Role role) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        HashMap<String, String> fieldsMap = appointmentDTO.getMap();

        setFieldsAndRightsToDTOAccordingToPolicy(fieldsMap, appointment, email, role);

        return appointmentDTO;
    }

    private void setFieldsAndRightsToDTOAccordingToPolicy(HashMap<String, String> fieldsMap, Appointment appointment, String email, Role role) {
        fieldsMap.put("id", Integer.toString(appointment.getId()));
        fieldsMap.put("rights_id", HIDDEN);
        fieldsMap.put("date", appointment.getAppointmentDate().toString());
        fieldsMap.put("rights_date", READONLY);
        fieldsMap.put("time", Byte.toString(appointment.getAppointmentTime()));
        fieldsMap.put("rights_time", READONLY);
        fieldsMap.put("serviceType", appointment.getServiceType().name());
        fieldsMap.put("rights_serviceType", READONLY);

        if (role.equals(Role.ROLE_USER)) {
            addFieldsForUser(fieldsMap, appointment, email);
        } else if (role.equals(Role.ROLE_MASTER)) {
            addFieldsForMaster(fieldsMap, appointment, email);
        } else if (role.equals(Role.ROLE_ADMIN)) {
            addFieldsForAdmin(fieldsMap, appointment);
        }
    }

    private void addFieldsForUser(HashMap<String, String> fieldsMap, Appointment appointment, String email) {
        if (email.equals(appointment.getCustomer().getEmail())) {
            fieldsMap.put("customer_name", appointment.getCustomer().getFirstName());
            fieldsMap.put("rights_customer_name", READONLY);

            if (appointment.isServiceProvided()) {
                fieldsMap.put("serviceProvided", Boolean.toString(appointment.isServiceProvided()));
                fieldsMap.put("rights_serviceProvided", READONLY);
            }
        }
    }

    private void addFieldsForMaster(HashMap<String, String> fieldsMap, Appointment appointment, String email) {
        if (email.equals(appointment.getMaster().getEmail())) {
            fieldsMap.put("customer_name", appointment.getCustomer().getFirstName());
            fieldsMap.put("rights_customer_name", READONLY);
            fieldsMap.put("master_name", appointment.getMaster().getFirstName());
            fieldsMap.put("rights_master_name", READONLY);

            fieldsMap.put("serviceProvided", Boolean.toString(appointment.isServiceProvided()));
            if (appointment.isServiceProvided()) {
                fieldsMap.put("rights_serviceProvided", READONLY);
            } else {
                fieldsMap.put("rights_serviceProvided", WRITE);
            }
        } else if (email.equals(appointment.getCustomer().getEmail())) {
            if (appointment.isServiceProvided()) {
                fieldsMap.put("serviceProvided", Boolean.toString(appointment.isServiceProvided()));
                fieldsMap.put("rights_serviceProvided", READONLY);
            }
        }
    }

    private void addFieldsForAdmin(HashMap<String, String> fieldsMap, Appointment appointment) {
        fieldsMap.put("customer_email", appointment.getCustomer().getEmail());
        fieldsMap.put("rights_customer_email", HIDDEN);

        fieldsMap.put("customer_name", userService.getFullNamePlusTelNumber(appointment.getCustomer()));
        fieldsMap.put("rights_customer_name", READONLY);

        fieldsMap.put("master_email", appointment.getMaster().getEmail());
        fieldsMap.put("rights_master_email", HIDDEN);

        fieldsMap.put("master_name", userService.getFullNamePlusTelNumber(appointment.getMaster()));
        fieldsMap.put("rights_master_name", READONLY);

        fieldsMap.put("serviceProvided", Boolean.toString(appointment.isServiceProvided()));
        fieldsMap.put("rights_serviceProvided", READONLY);
    }
}