package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Entity
@Getter
@Setter
public class Faculty {

    @Id
    @Pattern(regexp = "/^\\d{3}-?\\d{2}-?\\d{4}$/", message = "must be a ssn")
    private String ssn;

    private String rank;

    private Integer courseLoad;

    @OneToMany(mappedBy = "faculty")
    private Set<Section> sections;

}
