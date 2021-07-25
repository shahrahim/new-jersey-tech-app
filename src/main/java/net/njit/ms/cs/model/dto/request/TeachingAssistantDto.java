package net.njit.ms.cs.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import net.njit.ms.cs.model.entity.Section;

import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Getter
@Setter
public class TeachingAssistantDto {

    private String ssn;

    private String sid;

    private Integer workHours;

}
