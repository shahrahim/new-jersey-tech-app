package net.njit.ms.cs.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class FacultyResponse {

    private String ssn;

    private String rank;

    private Integer courseLoad;

    private Set<NumberDto> sections = new HashSet<>();

    private Set<CodeDto> departments = new HashSet<>();
}
