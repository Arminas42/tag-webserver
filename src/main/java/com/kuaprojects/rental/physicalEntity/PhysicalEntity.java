package com.kuaprojects.rental.physicalEntity;

import com.kuaprojects.rental.tag.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
// TODO: create strategy for business logic
public class PhysicalEntity {
    @GeneratedValue
    @Id
    private Long id;
    private String name;
    //    TODO: implement
    private String location = "default";
    //    TODO: implement
    private boolean multipleTags = false;
    @OneToMany
    private List<Tag> associatedTags;
}
