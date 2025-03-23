package com.kuaprojects.rental.integration;

import com.kuaprojects.rental.TestUtil;
import com.kuaprojects.rental.rent.RentRepository;
import com.kuaprojects.rental.trailer.Trailer;
import com.kuaprojects.rental.trailer.TrailerRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Disabled
public class RentRepositoryTest {

    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private TrailerRepository trailerRepository;

    @Test
    void test_findAllByRentTimestampBetween_method(){
        var trailer = trailerRepository.save(new Trailer("newTrailer", "TRAILER_200_CM"));
        rentRepository.save(TestUtil.createRent(LocalDateTime.now().plusDays(1),LocalDateTime.now().plusDays(2), trailer));
        rentRepository.save(TestUtil.createRent(LocalDateTime.now().plusDays(3),LocalDateTime.now().plusDays(4), trailer));

        var result = rentRepository.findAllByRentEndTimestampBetween(LocalDateTime.now(),LocalDateTime.now().plusDays(5));

        assertEquals(2, result.size());

    }
}
