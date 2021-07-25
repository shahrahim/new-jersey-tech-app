package net.njit.ms.cs.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDto {

    private Integer number;

    private String departmentCode;

    private String name;

    private Integer credits;

    private Integer taHours;

}
