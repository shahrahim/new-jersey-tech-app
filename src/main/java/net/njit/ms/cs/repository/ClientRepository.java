package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {
}
