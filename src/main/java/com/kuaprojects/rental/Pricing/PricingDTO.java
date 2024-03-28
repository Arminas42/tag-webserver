package com.kuaprojects.rental.Pricing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class PricingDTO {
    private String typeOfTrailer;
    private String parkLocation;
    private String paymentStrategy;
    private int costByHour;
    private int costByDay;
}
