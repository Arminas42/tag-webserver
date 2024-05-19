package com.kuaprojects.rental.tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
class TagController {

    private final TagService service;
    private final TagRepository tagRepository;
    private final TagDetectionRepository tagDetectionRepository;

    public TagController(TagService service, TagRepository repository, TagDetectionRepository tagDetectionRepository) {
        this.service = service;
        this.tagRepository = repository;
        this.tagDetectionRepository = tagDetectionRepository;
    }

    @PostMapping("tag/{tagId}")
    ResponseEntity<Tag> saveTag(@PathVariable String tagId) {
        var tag = service.saveTag(tagId);
        return ResponseEntity.ok(tag);
    }

    @PostMapping("tag/rfid/{tagId}")
    ResponseEntity<TagDetection> detectedTag(@PathVariable String tagId) {
        var tag = tagRepository.findByTagDeviceCode(tagId)
                .orElseThrow(() -> new RuntimeException("Tag does not exist in database"));
        var tagDetection = tagDetectionRepository.save(TagDetection.builder()
                .timeOfDetection(LocalDateTime.now())
                .tag(tag)
                .build());
        return ResponseEntity.ok(tagDetection);
    }
}
