package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@IdClass(TeachingAssistantId.class)
@Getter
@Setter
public class TeachingAssistant {

    @Id
    private String ssn;

    @Id
    private String studentSid;

    private Integer workHours;

    @ManyToMany
    private Set<Section> sections;

}
