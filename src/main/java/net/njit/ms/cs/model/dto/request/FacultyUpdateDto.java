package net.njit.ms.cs.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class FacultyUpdateDto {

    private String ssn;

    private String rank;

    private Integer courseLoad;

    private Set<String> departmentsToAdd = new HashSet<>();

    private Set<String> departmentsToRemove = new HashSet<>();

}
