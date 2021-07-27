package net.njit.ms.cs.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import net.njit.ms.cs.model.entity.RoomId;
import net.njit.ms.cs.model.entity.SectionId;

@Getter
@Setter
public class SectionRoomResponse {

    private SectionId section;

    private RoomId room;

    private String weekday;

    private String time;

}
