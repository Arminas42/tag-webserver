package com.kuaprojects.rental.tag;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Tag {

    public Tag(String tagCode){
        this.tagCode = tagCode;
    }
    @GeneratedValue
    @Id
    private Long id;
    private String tagCode;
}
