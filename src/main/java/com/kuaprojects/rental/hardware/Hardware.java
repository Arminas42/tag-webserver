package com.kuaprojects.rental.hardware;

import jakarta.persistence.Column;
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
public class Hardware {
    @GeneratedValue
    @Id
    private Long id;
    @Column(unique = true)
    private String hardwareCode;
    private String name;
    private String location;
    private LocalDateTime lastCheckedIn;
}
