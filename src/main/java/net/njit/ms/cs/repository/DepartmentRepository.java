package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, String> {
}
