package spring.scheduler.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spring.scheduler.dto.AppointmentDTO;
import spring.scheduler.exception.ExceptionKind;
import spring.scheduler.exception.ExtendedRuntimeException;
import spring.scheduler.service.AppointmentService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController()
public class AppointmentRestController {
    private AppointmentService appointmentService;

    @Autowired
    public AppointmentRestController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @RequestMapping(value = "/api/appointments/{start}/{end}", method = RequestMethod.GET)
    @ResponseBody
    public List<AppointmentDTO> getAppointmentsDTO(@PathVariable(name = "start", required = true) String strDateStart,
                                                   @PathVariable(name = "end", required = true) String strDateEnd) {

        LocalDate dateStart = stringToDate(strDateStart);
        LocalDate dateEnd = stringToDate(strDateEnd);
        if (dateStart.isAfter(dateEnd)) {
            throw new ExtendedRuntimeException(ExceptionKind.WRONG_DATA_PASSED);
        }

        return appointmentService.getAllAppointmentsDTO(strDateStart, strDateEnd);
    }

    @RequestMapping(value = "/api/appointments", method = RequestMethod.POST)
    @ResponseBody
    public boolean addAppointment(@RequestBody AppointmentDTO appointmentDTO) throws IOException {
        return appointmentService.addAppointment(appointmentDTO);
    }

    @RequestMapping(value = "/api/appointments", method = RequestMethod.PUT)
    @ResponseBody
    public boolean updateServiceProvided(@RequestBody AppointmentDTO appointmentDTO) throws IOException {
        return appointmentService.updateServiceProvided(appointmentDTO);
    }

    private LocalDate stringToDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            return LocalDate.parse(stringDate, formatter);
        } catch (Exception e) {
            log.error("wrong date: " + stringDate);
            throw new ExtendedRuntimeException(ExceptionKind.WRONG_DATA_PASSED, e);
        }
    }
}