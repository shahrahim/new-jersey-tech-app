package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="departmentCode", nullable=false)
    private Department department;

    @Pattern(regexp="/^\\d{3}-?\\d{2}-?\\d{4}$/",message="must be a ssn")
    private String ssn;

    private String name;

    private String address;

    private String highSchool;

    @Pattern(regexp="^\\d{4}$",message="must be a year")
    private String year;


}