package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, String> {
}
