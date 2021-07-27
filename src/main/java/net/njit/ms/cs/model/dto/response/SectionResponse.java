package net.njit.ms.cs.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import net.njit.ms.cs.model.entity.RoomId;

import java.util.Set;

@Getter
@Setter
public class SectionResponse {

    private Integer number;

    private String facultySsn;

    private Integer courseNumber;

    private String year;

    private String semester;

    private Set<SsnDto> teachingAssistants;

    private Set<SidDto> students;

    private Set<RoomId> rooms;

    private Integer maxEnroll;

}
