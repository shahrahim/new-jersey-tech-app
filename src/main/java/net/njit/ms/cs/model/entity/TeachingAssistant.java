package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@IdClass(TeachingAssistantId.class)
@Getter
@Setter
public class TeachingAssistant {

    @Id
    private String ssn;

    @Id
    private String sid;

    private Integer workHours;

    @ManyToMany(mappedBy = "teachingAssistants")
    private Set<Section> sections = new HashSet<>();

}
