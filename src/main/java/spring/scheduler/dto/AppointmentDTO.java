package spring.scheduler.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Getter
@Setter
@ToString
public class AppointmentDTO {
    private HashMap<String, String> map;

    public AppointmentDTO() {
        this.map = new HashMap<String, String>();
    }

    public AppointmentDTO(HashMap<String, String> fieldsMap) {
        this.map = fieldsMap;
    }
}