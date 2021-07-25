package net.njit.ms.cs.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffDto {

    private String ssn;

    private String name;

    private String address;

    private Integer salary;
}
