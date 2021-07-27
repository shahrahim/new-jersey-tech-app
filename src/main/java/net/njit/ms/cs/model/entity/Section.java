package net.njit.ms.cs.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@IdClass(SectionId.class)
@Getter
@Setter
public class Section {

    @Id
    private Integer number;

    @Id
    private Integer courseNumber;

    private String facultySsn;

    private String year;

    private String semester;

    @ManyToMany
    @JsonBackReference
    private Set<Staff> teachingAssistants;

    @ManyToMany
    private Set<Student> students;

    @ManyToMany
    private Set<Room> rooms;

    private Integer maxEnroll;

}