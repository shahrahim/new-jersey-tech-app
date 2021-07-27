package net.njit.ms.cs.repository;

import net.njit.ms.cs.model.entity.Section;
import net.njit.ms.cs.model.entity.SectionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, SectionId> {

    List<Section> findAllByFacultySsn(String ssn);

}
