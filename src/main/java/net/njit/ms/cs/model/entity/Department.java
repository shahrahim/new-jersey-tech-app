package net.njit.ms.cs.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Department {

    @Id
    private String code;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "buildingNumber")
    private Building building;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Student> students;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Course> courses;

    @ManyToMany
    private Set<Faculty> faculties;

    private String chairSsn;

    private String name;

}
