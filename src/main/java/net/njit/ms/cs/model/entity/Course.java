package net.njit.ms.cs.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Course {

    @Id
    private Integer number;

    @NotNull
    private String departmentCode;

    @OneToMany(mappedBy = "courseNumber", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Section> sections = new HashSet<>();

    private String name;

    private Integer credits;

    private Integer taHours;

}