package com.kuaprojects.rental.trailer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class TrailerController {
    private TrailerRepository repository;

    public TrailerController(TrailerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/trailers")
    List<Trailer> getAll(){
        return repository.findAll();
    }

    @GetMapping("/trailers/{id}")
    Trailer getTrailer(@PathVariable Long id){
        return repository.findById(id)
                .orElseThrow(() -> new TrailerNotFoundException(id));
    }
}
