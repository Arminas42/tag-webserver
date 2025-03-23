package com.kuaprojects.rental.integration;

import com.kuaprojects.rental.security.ApiKey;
import com.kuaprojects.rental.security.ApiKeyRepository;
import com.kuaprojects.rental.tag.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HappyPath {

    private static final Logger log = LoggerFactory.getLogger(HappyPath.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ApiKeyRepository repository;

    private final static String LOCALHOST = "http://localhost:";
    private final static String ENDPOINT = "/api/tag/rfid/";

    @BeforeEach
    void setupDatabase() {
        repository.save(new ApiKey(88L, "test", "$2y$12$OA01eN6LWCrAq6ZGNDt1nOeSrUALmETSxGsfT56fT0m9CkpYapivK"));
    }


    @Test
    void tagDetectionIsSavedToDb() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-KEY", "testingkey!@#_test");
        HttpEntity<String> request = new HttpEntity<>("", headers);

        String tagId = "111";
        String url = LOCALHOST + port + ENDPOINT + tagId;

        var tag = this.restTemplate.postForObject(url, request, Tag.class);

        assertThat(tag.getTagCode()).isEqualTo(tagId);
    }

}
