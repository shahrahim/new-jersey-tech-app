package net.njit.ms.cs.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FacultyDto {

    private String ssn;

    private String rank;

    private Integer courseLoad;

    private Set<String> departments;

}
