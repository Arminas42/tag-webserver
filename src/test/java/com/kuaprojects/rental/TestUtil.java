package com.kuaprojects.rental;

import com.kuaprojects.rental.Pricing.PaymentStrategy;
import com.kuaprojects.rental.Pricing.Pricing;
import com.kuaprojects.rental.Rent.Rent;
import com.kuaprojects.rental.Rent.RentStatus;
import com.kuaprojects.rental.Trailer.Trailer;
import com.kuaprojects.rental.Trailer.TrailerType;

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
