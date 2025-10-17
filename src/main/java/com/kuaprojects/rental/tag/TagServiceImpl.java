package com.kuaprojects.rental.tag;

import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagDetectionRepository tagDetectionRepository;

    public TagServiceImpl(TagRepository tagRepository, TagDetectionRepository tagDetectionRepository) {
        this.tagRepository = tagRepository;
        this.tagDetectionRepository = tagDetectionRepository;
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
    public void deleteTag(Long id) {
        var tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No tag found with id: " + id));
        tagRepository.delete(tag);
    }

}
