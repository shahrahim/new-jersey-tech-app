package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RoomId implements Serializable {

    private Integer roomNumber;

    private Integer buildingNumber;

    public RoomId(Integer roomNumber, Integer buildingNumber) {
        this.roomNumber = roomNumber;
        this.buildingNumber = buildingNumber;
    }

    public RoomId() {}

}
