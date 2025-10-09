package com.kuaprojects.rental.frontend.endpoint;

import com.kuaprojects.rental.tag.TagDetection;
import com.kuaprojects.rental.tag.TagDetectionRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class TagService {
    private final TagDetectionRepository repository;

    public TagService(TagDetectionRepository repository) {
        this.repository = repository;
    }

    public List<TagDetection> getDetections() {
        return repository.findAll();
    }

    public Page<TagDetection> getPagedDetections(int pageSize, int page) {
        return repository.findAll(PageRequest.of(page, pageSize));
    }
}
