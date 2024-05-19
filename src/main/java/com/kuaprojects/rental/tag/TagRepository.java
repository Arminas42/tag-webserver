package com.kuaprojects.rental.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository  extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagDeviceCode(String tagDeviceCode);
}
