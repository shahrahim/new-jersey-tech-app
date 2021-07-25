package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Entity
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sid;

    @NotNull
    @ManyToOne
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name="departmentCode", nullable=false)
    private Department department;

    @Pattern(regexp="/^\\d{3}-?\\d{2}-?\\d{4}$/",message="must be a ssn")
    private String ssn;

    private String name;

    private String address;

    private String highSchool;

    @Pattern(regexp="^\\d{4}$",message="must be a year")
    private String year;

    @ManyToMany
    private Set<Section> sections;



}