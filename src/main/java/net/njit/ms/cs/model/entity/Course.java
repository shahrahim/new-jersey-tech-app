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
public class Course {

    @Id
    private Integer number;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "departmentCode", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Section> sections = new HashSet<>();

    private String name;

    private Integer credits;

    private Integer taHours;

}