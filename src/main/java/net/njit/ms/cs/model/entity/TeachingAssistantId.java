package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TeachingAssistantId implements Serializable {

    private String ssn;

    private String sid;

    public TeachingAssistantId(String ssn, String sid) {
        this.ssn = ssn;
        this.sid = sid;
    }

    public TeachingAssistantId() {}

}