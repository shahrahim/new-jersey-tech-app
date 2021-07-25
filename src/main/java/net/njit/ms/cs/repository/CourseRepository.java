package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
