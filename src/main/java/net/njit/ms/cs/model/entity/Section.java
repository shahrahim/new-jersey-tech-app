package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Section {

    @Id
    private Integer number;

    @ManyToOne
    @JoinColumn(name = "facultySsn", nullable = false)
    private Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "courseNumber", nullable = false)
    private Course course;

    @ManyToMany
    private Set<TeachingAssistant> teachingAssistants;

    @ManyToMany
    private Set<Student> students;

    @ManyToMany
    private Set<Room> rooms;


    private Integer maxEnroll;

}