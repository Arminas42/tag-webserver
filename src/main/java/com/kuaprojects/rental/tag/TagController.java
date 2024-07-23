package com.kuaprojects.rental.tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
class TagController {

    private final TagService service;
//    private final TagRepository tagRepository;
    private final TagDetectionRepository tagDetectionRepository;

    public TagController(TagService service, TagRepository repository, TagDetectionRepository tagDetectionRepository) {
        this.service = service;
//        this.tagRepository = repository;
        this.tagDetectionRepository = tagDetectionRepository;
    }

    @PostMapping("tag/{tagId}")
    ResponseEntity<Tag> saveTag(@PathVariable String tagId) {
        var tag = service.saveTag(tagId);
        return ResponseEntity.ok(tag);
    }

    @PostMapping("tag/rfid/{tagId}")
    ResponseEntity<TagDetection> detectedTag(@PathVariable String tagId) {
    //TODO: Introduce when tag assignment logic is decided
        //        var tag = tagRepository.findByTagDeviceCode(tagId)
//                .orElseThrow(() -> new RuntimeException("Tag does not exist in database"));
        var tagDetection = tagDetectionRepository.save(TagDetection.builder()
                .timeOfDetection(LocalDateTime.now())
                .tagCode(tagId)
                .build());
        return ResponseEntity.ok(tagDetection);
    }

    @GetMapping("tag/rfid")
    ResponseEntity<List<TagDetection>> getDetectedTags(){
        return ResponseEntity.ok(tagDetectionRepository.findAll());
    }
}
