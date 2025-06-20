package com.kuaprojects.rental.configuration;

import com.kuaprojects.rental.rent.Rent;
import com.kuaprojects.rental.rent.RentRepository;
import com.kuaprojects.rental.rent.RentStatus;
import com.kuaprojects.rental.security.ApiKey;
import com.kuaprojects.rental.security.ApiKeyRepository;
import com.kuaprojects.rental.tag.Tag;
import com.kuaprojects.rental.tag.TagDetection;
import com.kuaprojects.rental.tag.TagDetectionRepository;
import com.kuaprojects.rental.tag.TagRepository;
import com.kuaprojects.rental.trailer.Trailer;
import com.kuaprojects.rental.trailer.TrailerRepository;
import com.kuaprojects.rental.user.AppUserDTO;
import com.kuaprojects.rental.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.kuaprojects.rental.configuration.StaticData.getStaticTagDetections;

@Configuration
public class LoadDB {

    private static final Logger log = LoggerFactory.getLogger(LoadDB.class);

    @Bean
    CommandLineRunner addAdminKey(ApiKeyRepository repository) {
        return args -> {
            repository.save(new ApiKey(1L, "iot", "$2y$12$Iy6tB.i35DrrtULLfMsml.EpKjehLTPFQOXVVKrkcIfILFhX2sIkW"));
        };
    }

    @ConditionalOnProperty(
            prefix = "preload.database",
            value = "enabled",
            havingValue = "true")
    @Bean
    @Profile("dev")
    CommandLineRunner initDatabase(TrailerRepository trailerRepository, RentRepository rentRepository, TagRepository tagRepository, TagDetectionRepository tagDetectionRepository, UserService userService) {

        return args -> {
            var trailer = trailerRepository.save(new Trailer("newTrailer", "TRAILER_200_CM"));
            log.info("Preloading " + trailer);
            log.info("Preloading " + trailerRepository.save(new Trailer("Car trailer", "TRAILER_200_CM")));
            log.info("Preloading " + trailerRepository.save(new Trailer("Trailer with tent", "TRAILER_WITH_TENT_200_CM")));
            log.info("Preloading " + trailerRepository.save(new Trailer("Car trawl", "TRAWL_BIG")));
            log.info("Preloading " + trailerRepository.save(new Trailer("Car trawl", "TRAWL_SMALL")));
            log.info("Preloading " + tagRepository.save(new Tag("Tag with no trailer")));
            log.info("Preloading " + tagRepository.save(new Tag("Tag with trailer", trailer)));

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
            log.info("Preloading fake tags");
            preloadFakeTags(tagDetectionRepository);
            log.info("Preloading tag detections");
            var detections = getStaticTagDetections();
            for (TagDetection td : detections
            ) {
                tagDetectionRepository.save(td);

            }
            userService.createUser(AppUserDTO.builder().username("admin").password("admin").build());
        };
    }

    private void preloadFakeTags(TagDetectionRepository tagDetectionRepository) {
        for (int i = 0; i < 10; i++) {
            tagDetectionRepository.save(new TagDetection((long) i, "test_tag_" + i, LocalDateTime.now()));
        }
    }


}
