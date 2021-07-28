package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.SectionRoom;
import net.njit.ms.cs.model.entity.SectionRoomId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRoomRepository extends JpaRepository<SectionRoom, SectionRoomId> {
}
