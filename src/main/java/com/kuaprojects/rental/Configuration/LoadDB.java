package com.kuaprojects.rental.Configuration;

import com.kuaprojects.rental.Trailer.Trailer;
import com.kuaprojects.rental.Trailer.TrailerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDB {
    private static final Logger log = LoggerFactory.getLogger(LoadDB.class);

    @ConditionalOnProperty(
            prefix = "preload.database",
            value = "enabled",
            havingValue = "true")
    @Bean
    CommandLineRunner initDatabase(TrailerRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Trailer("newTrailer", "TRAILER_200_CM")));
            log.info("Preloading " + repository.save(new Trailer("Car trailer", "TRAILER_200_CM")));
        };
    }
}
