package net.njit.ms.cs.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class StaffDto {

    private String ssn;

    private String type;

    private Set<String> departments = new HashSet<>();

    private String name;

    private String address;

    private Integer salary;

    private String rank;

    private Integer courseLoad;

    private Integer workHours;

}
