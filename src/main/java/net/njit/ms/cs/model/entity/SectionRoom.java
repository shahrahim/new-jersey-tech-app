package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Getter
@Setter
@IdClass(SectionRoomId.class)
@Entity
public class SectionRoom {

    @Id
    @ManyToOne
    private Section section;

    @Id
    @ManyToOne
    private Room room;

    @Id
    private String weekday;

    @Id
    private String time;
}
