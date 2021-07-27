package net.njit.ms.cs.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Department {

    @Id
    private String code;

    private Integer buildingNumber;

    @OneToMany(mappedBy = "departmentCode")
    @JsonBackReference
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "departmentCode", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(mappedBy = "departments")
    @JsonBackReference
    private Set<Staff> faculties = new HashSet<>();

    private String chairSsn;

    private String name;

    private Integer budget;

}
