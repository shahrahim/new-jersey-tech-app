package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

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

    @ManyToMany
    private Set<Section> sections;

    private Integer capacity;

    private String audioVisual;

}