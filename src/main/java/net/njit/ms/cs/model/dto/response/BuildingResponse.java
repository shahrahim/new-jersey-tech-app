package net.njit.ms.cs.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.model.entity.Room;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BuildingResponse {

    private Integer number;

    private Set<CodeDto> departments = new HashSet<>();

    private Set<NumberDto> rooms = new HashSet<>();

    private String name;

    private String location;

}
