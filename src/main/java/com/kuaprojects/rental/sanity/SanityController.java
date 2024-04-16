package com.kuaprojects.rental.sanity;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SanityController {

    @GetMapping("/sanityCheck")
    ResponseEntity<String> checkSanity(){
        return ResponseEntity.ok("Alive");
    }
}
