package net.njit.ms.cs.model.entity;

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
    @ManyToOne
    @JoinColumn(name = "facultySsn", nullable = false)
    private Faculty faculty;

    @Id
    @ManyToOne
    @JoinColumn(name = "courseNumber", nullable = false)
    private Course course;

    @Id
    private String sectionNumber;

    @ManyToMany
    private Set<TeachingAssistant> teachingAssistants;

    @ManyToMany
    private Set<Student> students;

    @ManyToMany
    private Set<Room> rooms;


    private Integer maxEnroll;

}