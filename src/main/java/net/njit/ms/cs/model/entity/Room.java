package net.njit.ms.cs.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Room {

    @Id
    private Integer roomNumber;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "buildingNumber")
    private Building building;

    @ManyToMany
    private Set<Section> sections;

    private Integer capacity;

    private String audioVisual;

}