package net.njit.ms.cs.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Building {

    @Id
    private Integer number;

    @OneToMany(mappedBy="buildingNumber", cascade = CascadeType.ALL)
    private Set<Department> departments = new HashSet<>();

    @OneToMany(mappedBy="buildingNumber", cascade = CascadeType.ALL)
    private Set<Room> rooms = new HashSet<>();

    @NotNull
    private String name;

    private String location;

}