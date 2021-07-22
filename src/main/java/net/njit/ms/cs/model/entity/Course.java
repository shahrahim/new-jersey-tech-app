package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Course {

    @Id
    private Long number;

    @ManyToOne
    @JoinColumn(name = "departmentCode", nullable = false)
    private Department department;

    @OneToMany(mappedBy="course")
    private Set<Section> sections;

    private String name;

    private Integer credits;

    private Integer taHours;

}