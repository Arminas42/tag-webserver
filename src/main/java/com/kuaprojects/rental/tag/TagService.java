package com.kuaprojects.rental.tag;

public interface TagService {
//    Tag saveTag(String tagCode);
    Tag createTag(String tagCode);
    Tag assignTagToTrailer(Long id, Long trailerId);
    void deleteTag(Long id);

}
