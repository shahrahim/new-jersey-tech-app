package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@IdClass(RoomId.class)
@Getter
@Setter
public class Room {

    @Id
    @ManyToOne
    @JoinColumn(name = "buildingNumber", nullable = false)
    private Building building;

    @Id
    private String number;

    private Integer capacity;

    private String audioVisual;

}