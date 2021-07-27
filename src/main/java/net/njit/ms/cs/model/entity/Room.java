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

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Set<SectionRoom> sectionRooms = new HashSet<>();

    private Integer capacity;

    private String audioVisual;

}