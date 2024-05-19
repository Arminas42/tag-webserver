package com.kuaprojects.rental.tag;

import com.kuaprojects.rental.trailer.TrailerNotFoundException;
import com.kuaprojects.rental.trailer.TrailerRepository;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TrailerRepository trailerRepository;

    public TagServiceImpl(TagRepository tagRepository, TrailerRepository trailerRepository) {
        this.tagRepository = tagRepository;
        this.trailerRepository = trailerRepository;
    }

    @Override
    public Tag saveTag(String tagDeviceCode) {
        return tagRepository.save(
                Tag.builder()
                        .tagDeviceCode(tagDeviceCode)
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
