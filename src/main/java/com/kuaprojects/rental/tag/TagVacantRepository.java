package com.kuaprojects.rental.tag;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TagVacantRepository extends JpaRepository<TagVacant, Long> {
    List<TagVacant> findByTagCodeAndVacantFromBetween(String tagCode, LocalDateTime start, LocalDateTime end);

    List<TagVacant> findByTagCodeInAndVacantFromBetween(List<String> tagCodes, LocalDateTime start, LocalDateTime end);

    List<TagVacant> findByStatus(ProcessingStatus status);
}
