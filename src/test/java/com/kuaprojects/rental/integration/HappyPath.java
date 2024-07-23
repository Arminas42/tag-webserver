package com.kuaprojects.rental.integration;

import com.kuaprojects.rental.tag.Tag;
import com.kuaprojects.rental.tag.TagDetection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HappyPath {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final static String LOCALHOST = "http://localhost:";
    private final static String ENDPOINT = "/tag/";
    private final static String ENDPOINT_DETECTION = "/tag/rfid/";

    @Test
    void tagIsAddedToDB_and_tagDetectionEntityIsSavedToDB() throws Exception {
        // Admin adds tag to database
        tagIsSavedToDb();
        // UHF RFID device sends request when detecting UHF RFID tag
//        tagIsDetectedAndSavedToDb();

    }

    void tagIsSavedToDb() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-KEY", "my_custom_token");
        HttpEntity<String> request = new HttpEntity<>("", headers);

        String tagId = "111";
        String url = LOCALHOST + port + ENDPOINT + tagId;

        var tag = this.restTemplate.postForObject(url, request, Tag.class);

        assertThat(tag.getTagCode()).isEqualTo(tagId);
    }

//    void tagIsDetectedAndSavedToDb() throws Exception {
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("X-API-KEY", "my_custom_token");
//        HttpEntity<String> request = new HttpEntity<>("", headers);
//
//        String tagId = "111";
//        String url = LOCALHOST + port + ENDPOINT_DETECTION + tagId;
//
//        var tagDetection = this.restTemplate.postForObject(url, request, TagDetection.class);
//
//        assertThat(tagDetection.getTag().getTagDeviceCode()).isEqualTo(tagId);
//    }
}
