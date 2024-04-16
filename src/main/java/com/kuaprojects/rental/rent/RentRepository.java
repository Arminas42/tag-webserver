package com.kuaprojects.rental.rent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findAllByRentEndTimestampBetween(LocalDateTime rentStartTimestamp, LocalDateTime rentEndTimestamp);
}
