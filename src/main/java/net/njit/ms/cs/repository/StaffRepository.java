package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, String> {

}
