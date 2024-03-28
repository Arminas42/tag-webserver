package com.kuaprojects.rental.Rent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findAllByRentEndTimestampBetween(LocalDateTime rentStartTimestamp, LocalDateTime rentEndTimestamp);
}
