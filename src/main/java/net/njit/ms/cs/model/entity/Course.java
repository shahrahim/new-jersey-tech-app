package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

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

    @OneToMany(mappedBy="course", cascade = CascadeType.ALL)
    private Set<Section> sections;

    private String name;

    private Integer credits;

    private Integer taHours;

}