package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Integer> {

}
