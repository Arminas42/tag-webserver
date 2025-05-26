package com.kuaprojects.rental.tag;

public class TagAlreadyExistsException extends RuntimeException{

    public TagAlreadyExistsException(String tagCode) {
        super("Tag already exists: " + tagCode);
    }
}
