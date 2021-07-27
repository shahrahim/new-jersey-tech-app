package net.njit.ms.cs.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import net.njit.ms.cs.model.entity.RoomId;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class SectionDto {

    private Integer number;

    private Integer courseNumber;

    private String facultySsn;

    private String year;

    private String semester;

    private Set<String> teachingAssistantSsns = new HashSet<>();

    private Set<String> students = new HashSet<>();

    private Set<RoomId> rooms = new HashSet<>();

    private Integer maxEnroll;

}
