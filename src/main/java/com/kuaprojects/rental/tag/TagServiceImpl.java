package com.kuaprojects.rental.tag;

import com.kuaprojects.rental.trailer.TrailerNotFoundException;
import com.kuaprojects.rental.trailer.TrailerRepository;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagDetectionRepository tagDetectionRepository;
    private final TrailerRepository trailerRepository;

    public TagServiceImpl(TagRepository tagRepository, TrailerRepository trailerRepository, TagDetectionRepository tagDetectionRepository) {
        this.tagRepository = tagRepository;
        this.tagDetectionRepository = tagDetectionRepository;
        this.trailerRepository = trailerRepository;
    }

//    @Override
//    public Tag saveTag(String tagDeviceCode) {
//        return tagRepository.save(
//                Tag.builder()
//                        .tagCode(tagDeviceCode)
//                        .build());
//    }

    @Override
    public Tag createTag(String tagCode) {
        if(tagRepository.findByTagCode(tagCode).isPresent()) throw new TagAlreadyExistsException(tagCode);
        return tagRepository.save(Tag.builder()
                .tagCode(tagCode)
                .build());
    }

    @Override
    public Tag assignTagToTrailer(Long id, Long trailerId) {
        var tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No tag found with id: " + id));
        var trailer = trailerRepository.findById(trailerId).orElseThrow(() -> new TrailerNotFoundException(trailerId));

        tag.setTrailer(trailer);
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(Long id) {
        var tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No tag found with id: " + id));
        tagRepository.delete(tag);
    }

}
