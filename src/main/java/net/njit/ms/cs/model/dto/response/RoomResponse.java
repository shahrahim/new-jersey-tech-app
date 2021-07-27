package net.njit.ms.cs.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class RoomResponse {

    private Integer roomNumber;

    private Integer buildingNumber;

    private Set<RoomSectionInfo> sections = new HashSet<>();

    private Integer capacity;

    private String audioVisual;

}
