package com.kuaprojects.rental.rent;

import com.kuaprojects.rental.trailer.Trailer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rent implements Serializable {

    @GeneratedValue
    @Id
    private Long id;
    private RentStatus status;
    @ManyToOne
    private Trailer rentedTrailer;
    private String driverCarLicensePlate;
    private String driverFullName;
    private LocalDateTime rentStartTimestamp;
    private LocalDateTime rentEndTimestamp;

}
