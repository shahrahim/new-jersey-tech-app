package net.njit.ms.cs.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Integer number;

    @JsonBackReference
    @OneToMany(mappedBy="building", cascade = CascadeType.ALL)
    private Set<Department> departments;

    @JsonBackReference
    @OneToMany(mappedBy="building", cascade = CascadeType.ALL)
    private Set<Room> rooms;

    @NotNull
    private String name;

    private String location;

}