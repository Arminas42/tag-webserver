package com.kuaprojects.rental;

import com.kuaprojects.rental.pricing.PaymentStrategy;
import com.kuaprojects.rental.pricing.Pricing;
import com.kuaprojects.rental.rent.Rent;
import com.kuaprojects.rental.rent.RentStatus;
import com.kuaprojects.rental.trailer.Trailer;
import com.kuaprojects.rental.trailer.TrailerType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class TestUtil {

    private final static Trailer trailer = new Trailer("something", "TRAILER_200_CM");
    private final static Trailer trailer_2 = new Trailer("something", "TRAILER_300_CM");

    public static Pricing createPricing(PaymentStrategy paymentStrategy, BigDecimal costByHour, BigDecimal costByDay) {
        Pricing pricing = new Pricing();
        pricing.setPaymentStrategy(paymentStrategy);
        pricing.setCostByHour(costByHour);
        pricing.setCostByDay(costByDay);
        pricing.setTrailerType(TrailerType.valueOf("TRAILER_200_CM"));
        return pricing;
    }

    public static Rent createRent(LocalDateTime startTimestamp, LocalDateTime endTimestamp, Trailer trailerParameter) {
        Rent rent = new Rent();
        rent.setDriverCarLicensePlate("DDD555");
        rent.setDriverFullName("John Smith");
        rent.setStatus(RentStatus.ENDED);
        rent.setRentStartTimestamp(startTimestamp);
        rent.setRentEndTimestamp(endTimestamp);
        rent.setRentedTrailer(Objects.requireNonNullElse(trailerParameter, trailer));
        return rent;
    }
}
