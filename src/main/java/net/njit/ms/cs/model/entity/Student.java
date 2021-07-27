package net.njit.ms.cs.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Student {

    @Id
    private String sid;

    @Column(unique = true)
    @Pattern(regexp="/^\\d{3}-?\\d{2}-?\\d{4}$/",message="must be a ssn")
    private String ssn;

    private String departmentCode;

    private String name;

    private String address;

    private String highSchool;

    @Pattern(regexp="^\\d{4}$",message="must be a year")
    private String year;

    @ManyToMany(mappedBy = "students")
    private Set<Section> sections = new HashSet<>();

}