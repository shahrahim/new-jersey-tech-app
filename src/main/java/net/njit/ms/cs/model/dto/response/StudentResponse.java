package net.njit.ms.cs.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import net.njit.ms.cs.model.entity.SectionId;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class StudentResponse {

    private String sid;

    private String departmentCode;

    private String ssn;

    private String name;

    private String address;

    private String highSchool;

    private String year;

    private Set<SectionInfo> sections = new HashSet<>();

}
