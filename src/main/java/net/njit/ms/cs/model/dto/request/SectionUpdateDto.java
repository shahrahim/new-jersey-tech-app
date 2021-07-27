package net.njit.ms.cs.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import net.njit.ms.cs.model.entity.RoomId;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class SectionUpdateDto {

    private Integer number;

    private String facultySsn;

    private Integer courseNumber;

    private Set<String> teachingAssistantsToAdd = new HashSet<>();

    private Set<String> teachingAssistantsToRemove = new HashSet<>();

    private Set<String> studentsToAdd = new HashSet<>();

    private Set<String> studentsToRemove = new HashSet<>();

    private Set<RoomId> roomsToAdd = new HashSet<>();

    private Set<RoomId> roomsToRemove = new HashSet<>();

    private Integer maxEnroll;

}
