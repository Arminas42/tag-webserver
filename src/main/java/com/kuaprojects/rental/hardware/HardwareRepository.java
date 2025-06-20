package com.kuaprojects.rental.hardware;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HardwareRepository extends JpaRepository<Hardware, Long> {
    Optional<Hardware> findByHardwareCode(String hardwareCode);
}
