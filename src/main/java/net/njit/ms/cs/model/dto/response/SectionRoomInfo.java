package net.njit.ms.cs.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionRoomInfo {

    private Integer roomNumber;

    private Integer buildingNumber;

    private String weekday;

    private String time;

}
