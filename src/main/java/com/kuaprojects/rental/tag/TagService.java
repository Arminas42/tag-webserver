package com.kuaprojects.rental.tag;

public interface TagService {
//    Tag saveTag(String tagCode);
    Tag createTag(String tagCode);
    void deleteTag(Long id);

}
