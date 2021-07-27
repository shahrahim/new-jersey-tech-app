package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {

    List<Student> findAllBySsn(String ssn);
}
