package com.kuaprojects.rental.Configuration;

import com.kuaprojects.rental.Rent.Rent;
import com.kuaprojects.rental.Rent.RentRepository;
import com.kuaprojects.rental.Rent.RentStatus;
import com.kuaprojects.rental.Trailer.Trailer;
import com.kuaprojects.rental.Trailer.TrailerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class LoadDB {
    private static final Logger log = LoggerFactory.getLogger(LoadDB.class);

    @ConditionalOnProperty(
            prefix = "preload.database",
            value = "enabled",
            havingValue = "true")
    @Bean
    CommandLineRunner initDatabase(TrailerRepository trailerRepository, RentRepository rentRepository) {

        return args -> {
            var trailer = trailerRepository.save(new Trailer("newTrailer", "TRAILER_200_CM"));
            log.info("Preloading " + trailer);
            log.info("Preloading " + trailerRepository.save(new Trailer("Car trailer", "TRAILER_200_CM")));
            log.info("Preloading " + trailerRepository.save(new Trailer("Trailer with tent", "TRAILER_WITH_TENT_200_CM")));
            log.info("Preloading " + trailerRepository.save(new Trailer("Car trawl", "TRAWL_BIG")));
            log.info("Preloading " + trailerRepository.save(new Trailer("Car trawl", "TRAWL_SMALL")));

            log.info("Preloading rent: " + rentRepository.save(Rent.builder()
                    .driverCarLicensePlate("GGG 222")
                    .driverFullName("John Smith")
                    .rentStartTimestamp(LocalDateTime.now().minusHours(2))
                    .rentedTrailer(trailer)
                    .status(RentStatus.IN_PROCESS)
                    .build()));
            log.info("Preloading rent: " + rentRepository.save(Rent.builder()
                    .driverCarLicensePlate("DDD 222")
                    .driverFullName("John Smith")
                    .rentStartTimestamp(LocalDateTime.now().minusHours(5))
                    .rentEndTimestamp(LocalDateTime.now().minusHours(3))
                    .rentedTrailer(trailer)
                    .status(RentStatus.ENDED)
                    .build()));
        };
    }
}
