package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Integer> {
}
