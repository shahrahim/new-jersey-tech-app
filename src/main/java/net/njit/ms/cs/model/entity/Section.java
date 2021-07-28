package net.njit.ms.cs.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@IdClass(SectionId.class)
@Getter
@Setter
public class Section {

    @Id
    @Column(unique = true)
    private Integer number;

    @Id
    private Integer courseNumber;

    private String facultySsn;

    private String year;

    private String semester;

    @ManyToMany
    private Set<Staff> teachingAssistants = new HashSet<>();

    @ManyToMany
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private Set<SectionRoom> sectionRooms = new HashSet<>();

    private Integer maxEnroll;

}