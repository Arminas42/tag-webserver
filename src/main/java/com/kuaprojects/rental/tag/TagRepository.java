package com.kuaprojects.rental.tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository  extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagCode(String tagCode);
    Page<Tag> findAll(Pageable pageable);
    List<Tag> findByTagCodeContainingIgnoreCase(String tagCode);
}
