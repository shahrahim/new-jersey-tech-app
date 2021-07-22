package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RoomId implements Serializable {

    private Building building;

    private String number;


    public RoomId(Building building, String number) {
        this.building = building;
        this.number = number;
    }


}