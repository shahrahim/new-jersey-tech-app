package net.njit.ms.cs.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@IdClass(RoomId.class)
@Getter
@Setter
public class Room {

    @Id
    private Integer roomNumber;

    @Id
    private Integer buildingNumber;

    @ManyToMany(mappedBy = "rooms")
    private Set<Section> sections = new HashSet<>();

    private Integer capacity;

    private String audioVisual;

}