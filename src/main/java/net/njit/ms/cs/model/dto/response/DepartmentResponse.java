package net.njit.ms.cs.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class DepartmentResponse {

    private String code;

    private String name;

    private String chairSsn;

    private Integer buildingNumber;

    private Set<SidDto> students;

    private Set<NumberDto> courses;

    private Set<SsnDto> faculties;
}
