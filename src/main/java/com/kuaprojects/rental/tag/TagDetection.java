package com.kuaprojects.rental.tag;

import com.kuaprojects.rental.trailer.Trailer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
    @ManyToOne
    private Tag tag;
    private LocalDateTime timeOfDetection;

}
