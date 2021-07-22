package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TeachingAssistantId implements Serializable {

    private String ssn;

    private String studentSid;


    public TeachingAssistantId(String ssn, String studentSid) {
        this.ssn = ssn;
        this.studentSid = studentSid;
    }

}