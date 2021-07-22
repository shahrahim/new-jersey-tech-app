package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SectionId implements Serializable {

    private Faculty faculty;

    private Course course;

    private String sectionNumber;

    public SectionId(Faculty faculty, Course course, String sectionNumber) {
        this.faculty = faculty;
        this.course = course;
        this.sectionNumber = sectionNumber;
    }


}
