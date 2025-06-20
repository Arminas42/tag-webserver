package com.kuaprojects.rental.tag;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TagDetectionRepository  extends JpaRepository<TagDetection, Long> {
    List<TagDetection> findByTagCodeAndDetectionTimeBetween(String tagCode, LocalDateTime start, LocalDateTime end);
    List<TagDetection> findByDetectionTimeBetween(LocalDateTime start, LocalDateTime end);
    Page<TagDetection> findAll(Pageable pageable);
    Page<TagDetection> findAllByOrderByDetectionTimeDesc(Pageable pageable);

}
