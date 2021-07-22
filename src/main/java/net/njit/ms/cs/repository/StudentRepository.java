package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
}
