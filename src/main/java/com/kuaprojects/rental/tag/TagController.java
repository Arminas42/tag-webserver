package com.kuaprojects.rental.tag;

import com.kuaprojects.rental.configuration.ApiPrefixController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@ApiPrefixController
class TagController {

    private final TagService service;
    //    private final TagRepository tagRepository;
    private final TagDetectionRepository tagDetectionRepository;

    public TagController(TagService service, TagRepository repository, TagDetectionRepository tagDetectionRepository) {
        this.service = service;
//        this.tagRepository = repository;
        this.tagDetectionRepository = tagDetectionRepository;
    }

//    @PostMapping("tag/{tagId}")
//    ResponseEntity<Tag> saveTag(@PathVariable String tagId) {
//        var tag = service.saveTag(tagId);
//        return ResponseEntity.ok(tag);
//    }

    @PostMapping("tag/rfid/{tagId}")
    ResponseEntity<TagDetection> detectedTag(@PathVariable String tagId) {
        var tagDetection = tagDetectionRepository.save(TagDetection.builder()
                .detectionTime(LocalDateTime.now())
                .tagCode(tagId)
                .build());
        return ResponseEntity.ok(tagDetection);
    }

    @PostMapping("tag/rfid")
    ResponseEntity<String> detectedBulkTags(@RequestBody bulkTagDetectionDto tagsDto) {
        tagsDto.tags().stream().forEach(tag -> tagDetectionRepository.save(TagDetection.builder()
                .detectionTime(LocalDateTime.now())
                .tagCode(tag)
                .build()));
        return ResponseEntity.ok("proccesed number of tags: " + tagsDto.tags().size());
    }

    private record bulkTagDetectionDto(List<String> tags){}

    @GetMapping("tag/rfid")
    ResponseEntity<List<TagDetection>> getDetectedTags() {
        return ResponseEntity.ok(tagDetectionRepository.findAll());
    }
}
