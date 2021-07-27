package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SectionId implements Serializable {

    private Integer number;

    private Integer courseNumber;


    public SectionId(Integer number, Integer courseNumber) {
        this.number = number;
        this.courseNumber = courseNumber;
    }

    public SectionId() {}

}
