package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SectionRoomId implements Serializable {

    private SectionId section;

    private RoomId room;

    private String weekday;

    private String time;

    public SectionRoomId(SectionId section, RoomId room, String weekday, String time) {
        this.section = section;
        this.room = room;
        this.weekday = weekday;
        this.time = time;
    }

    public SectionRoomId() {}

}