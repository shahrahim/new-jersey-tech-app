package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Setter
@Getter
public class Staff {

    @Id
    @Pattern(regexp="/^\\d{3}-?\\d{2}-?\\d{4}$/",message="must be a ssn")
    private String ssn;

    @NotNull
    private String name;

    private String address;

    private Integer salary;
}
