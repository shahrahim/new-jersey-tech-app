package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.TeachingAssistant;
import net.njit.ms.cs.model.entity.TeachingAssistantId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeachingAssistantRepository extends JpaRepository<TeachingAssistant, TeachingAssistantId> {
}
