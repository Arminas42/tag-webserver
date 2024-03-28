package com.kuaprojects.rental.Pricing;

import com.kuaprojects.rental.Location.ParkLocation;
import com.kuaprojects.rental.Trailer.TrailerType;
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
import org.springframework.cache.annotation.Cacheable;

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
