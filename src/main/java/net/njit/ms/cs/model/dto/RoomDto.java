package net.njit.ms.cs.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDto {

    private Integer buildingNumber;

    private Integer roomNumber;

    private Integer capacity;

    private String audioVisual;

}
