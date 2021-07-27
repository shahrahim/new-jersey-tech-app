package net.njit.ms.cs.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import net.njit.ms.cs.model.entity.SectionId;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class CourseResponse {

    private Integer number;

    private String departmentCode;

    private Set<SectionInfo> sections = new HashSet<>();

    private String name;

    private Integer credits;

    private Integer taHours;

}
