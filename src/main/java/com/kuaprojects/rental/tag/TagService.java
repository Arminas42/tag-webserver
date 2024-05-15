package com.kuaprojects.rental.tag;

public interface TagService {
    Tag saveTag(String tagDeviceCode);
    Tag assignTagToTrailer(Long id, Long trailerId);
    void deleteTag(Long id);

}
