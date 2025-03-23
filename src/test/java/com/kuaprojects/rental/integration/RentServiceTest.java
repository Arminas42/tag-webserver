package com.kuaprojects.rental.integration;

import com.kuaprojects.rental.rent.RentNotFoundException;
import com.kuaprojects.rental.rent.RentRepository;
import com.kuaprojects.rental.rent.RentServiceImpl;
import com.kuaprojects.rental.rent.RentStatus;
import com.kuaprojects.rental.trailer.Trailer;
import com.kuaprojects.rental.trailer.TrailerNotFoundException;
import com.kuaprojects.rental.trailer.TrailerRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Disabled
public class RentServiceTest {

    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private TrailerRepository trailerRepository;
    @Autowired
    private RentServiceImpl rentService;

    private final static Long NOT_EXISTING_TRAILER_ID = 999L;
    private final static Long NOT_EXISTING_RENT_ID = 999L;

    @Test
    void test_createRent_method_saves_rent_entity(){
        var trailer = trailerRepository.save(new Trailer("newTrailer", "TRAILER_200_CM"));
        var rentToSave = rentService.createRent(trailer.getId(),"GGG555","John Smith");
        var savedRent = rentRepository.findById(rentToSave.getId());

        assertTrue(savedRent.isPresent());
        assertThat(savedRent.get())
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(rentToSave);
    }

    @Test
    void test_createRent_method_throws_error_when_trailer_not_found(){
        Exception exception = assertThrows(TrailerNotFoundException.class,
                () -> rentService.createRent(NOT_EXISTING_TRAILER_ID,"GGG555","John Smith"));
        String expectedMessage = "Could not find a trailer: " + NOT_EXISTING_TRAILER_ID;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void test_endsRent_method_updated_rent_entity_endRentTimestamp_field(){
        var trailer = trailerRepository.save(new Trailer("newTrailer", "TRAILER_200_CM"));
        var savedRentId = rentService.createRent(trailer.getId(),"GGG555","John Smith").getId();
        var updatedRent = rentService.endRent(savedRentId);

        assertNotNull(updatedRent.getRentEndTimestamp());
        assertThat(updatedRent.getRentEndTimestamp()).isExactlyInstanceOf(LocalDateTime.class);
        assertThat(updatedRent.getStatus()).isEqualTo(RentStatus.ENDED);
    }

    @Test
    void test_endRent_method_throws_error_when_rent_not_found(){
        Exception exception = assertThrows(RentNotFoundException.class, () -> rentService.endRent(NOT_EXISTING_RENT_ID));
        String expectedMessage = "Rent not found: " + NOT_EXISTING_RENT_ID;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }
}
