package net.njit.ms.cs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client")
@Getter
@Setter
public class Client {

    @Id
    private String name;

    private String email;
}
