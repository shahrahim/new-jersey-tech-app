package net.njit.ms.cs.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class StaffResponse {

    private String ssn;

    private String name;

    private Set<CodeDto> departments = new HashSet<>();

    private Set<SectionInfo> facultySections = new HashSet<>();

    private Set<SectionInfo> taSections = new HashSet<>();

    private String address;

    private Integer salary;

    private String type;

    private String rank;

    private Integer courseLoad;

    private Integer workHours;


}
