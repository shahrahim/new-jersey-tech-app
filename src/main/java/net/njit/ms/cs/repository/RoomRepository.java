package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
