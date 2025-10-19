package com.kuaprojects.rental.tag;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TagDetection {
    @GeneratedValue
    @Id
    private Long id;
    private String tagCode;
    private LocalDateTime detectionTime;
    private boolean softDelete = false;

    public TagDetection(Long id , String tagCode, LocalDateTime detectionTime) {
        this.detectionTime = detectionTime;
        this.tagCode = tagCode;
        this.id = id;
    }
}
