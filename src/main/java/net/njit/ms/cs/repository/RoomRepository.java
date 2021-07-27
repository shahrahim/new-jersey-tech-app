package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.Room;
import net.njit.ms.cs.model.entity.RoomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, RoomId> {
}
