package net.njit.ms.cs.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class DepartmentDto {

    private String code;

    private String name;

    private String chairSsn;

    private Long buildingNumber;
}
