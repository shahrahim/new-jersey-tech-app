package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer number;

    @OneToMany(mappedBy="building")
    private Set<Department> departments;

    @OneToMany(mappedBy="building")
    private Set<Room> rooms;

    @NotNull
    private String name;

    private String location;

}