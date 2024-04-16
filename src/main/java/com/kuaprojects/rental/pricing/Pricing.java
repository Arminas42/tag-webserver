package com.kuaprojects.rental.pricing;

import com.kuaprojects.rental.location.ParkLocation;
import com.kuaprojects.rental.trailer.TrailerType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pricing implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private PaymentStrategy paymentStrategy;
    private BigDecimal costByHour;
    private BigDecimal costByDay;
    @Enumerated(EnumType.STRING)
    private TrailerType trailerType;
    private ParkLocation trailerParkLocation;
}
