package net.njit.ms.cs.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class Staff {

    @Id
    @Pattern(regexp="/^\\d{3}-?\\d{2}-?\\d{4}$/",message="must be a ssn")
    private String ssn;

    @NotNull
    private String name;

    @ManyToMany
    @JoinTable(
            name = "faculty_departments",
            joinColumns = @JoinColumn(name = "ssn"),
            inverseJoinColumns = @JoinColumn(name = "code"))
    @JsonManagedReference
    private Set<Department> departments = new HashSet<>();

    @OneToMany(mappedBy = "facultySsn")
    private Set<Section> facultySections = new HashSet<>();

    @ManyToMany(mappedBy = "teachingAssistants")
    @JsonManagedReference
    private Set<Section> taSections = new HashSet<>();


    private String address;

    private Integer salary;

    private String type;

    private String rank;

    private Integer courseLoad;

    private Integer workHours;
}
