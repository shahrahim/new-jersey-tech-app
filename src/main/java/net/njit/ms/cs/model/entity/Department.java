package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
public class Department {

    @Id
    private String code;

    @ManyToOne
    @JoinColumn(name="buildingNumber")
    private Building building;

    @OneToMany(mappedBy="department")
    private Set<Student> students;

    @OneToMany(mappedBy="department")
    private Set<Course> courses;

    @ManyToMany
    private Set<Faculty> faculties;

    private String chairSsn;

    private String name;

}
