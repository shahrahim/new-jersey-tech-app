package net.njit.ms.cs.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import net.njit.ms.cs.model.entity.Section;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class StudentDto {

    private String sid;

    private String departmentCode;

    private String ssn;

    private String name;

    private String address;

    private String highSchool;

    private String year;

//    private Set<Section> sections = new HashSet<>();

}
