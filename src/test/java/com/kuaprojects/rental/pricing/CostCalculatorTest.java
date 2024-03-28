package com.kuaprojects.rental.pricing;

import com.kuaprojects.rental.Pricing.CostCalculatorImpl;
import com.kuaprojects.rental.Pricing.PaymentStrategy;
import com.kuaprojects.rental.Pricing.Pricing;
import com.kuaprojects.rental.Rent.Rent;
import com.kuaprojects.rental.Trailer.Trailer;
import com.kuaprojects.rental.Trailer.TrailerType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.kuaprojects.rental.TestUtil.createPricing;
import static com.kuaprojects.rental.TestUtil.createRent;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CostCalculatorTest {


//    @Test
    void test_calculate_time_difference() {
        var currentTime = LocalDateTime.now();
        var sixHourTimeDifference = currentTime.minusHours(6L);
        Long timeDifference = HOURS.between(currentTime, sixHourTimeDifference);
        System.out.println("Time difference: " + timeDifference);
        for (var value: TrailerType.values()) {
        System.out.println(value);

        }
        var mytype = TrailerType.valueOf("TRAILER_200_CM");
        System.out.println("Type name: *** " + mytype.name());
        assertNotNull(timeDifference);

    }

    @Test
    void testCalculateSingleRentCost_ByDay() {
        CostCalculatorImpl costCalculator = new CostCalculatorImpl();
        Pricing pricing = createPricing(PaymentStrategy.BY_DAY, BigDecimal.valueOf(5.0), BigDecimal.valueOf(15));
        Rent rent = createRent(LocalDateTime.now(), LocalDateTime.now().plusDays(2), null);

        BigDecimal cost = costCalculator.calculateSingleRentCost(pricing, rent);

        assertEquals(BigDecimal.valueOf(30.0), cost);
    }

    @Test
    void testCalculateSingleRentCost_ByHour() {
        CostCalculatorImpl costCalculator = new CostCalculatorImpl();
        Pricing pricing = createPricing(PaymentStrategy.BY_HOUR, BigDecimal.valueOf(10.0), BigDecimal.valueOf(100.0));
        Rent rent = createRent(LocalDateTime.now(), LocalDateTime.now().plusHours(5), null);

        BigDecimal cost = costCalculator.calculateSingleRentCost(pricing, rent);

        assertEquals(BigDecimal.valueOf(50.0), cost);
    }


    /**
     * If hourly calculation is bigger than by day cost start calculating by day
     */
    @Test
    void testCalculateSingleRentCost_ByHour_HourlyCostExceedsDayCost() {
        CostCalculatorImpl costCalculator = new CostCalculatorImpl();
        Pricing pricing = createPricing(PaymentStrategy.BY_HOUR, BigDecimal.valueOf(20.0), BigDecimal.valueOf(80.0));
        Rent rent = createRent(LocalDateTime.now(), LocalDateTime.now().plusHours(5), null);

        BigDecimal cost = costCalculator.calculateSingleRentCost(pricing, rent);

        assertEquals(BigDecimal.valueOf(80.0), cost);
    }

    @Test
    void testCalculateSingleRentCost_InvalidPaymentStrategy() {
        CostCalculatorImpl costCalculator = new CostCalculatorImpl();
        Pricing pricing = createPricing(null, BigDecimal.ZERO, BigDecimal.ZERO);
        Rent rent = createRent(LocalDateTime.now(), LocalDateTime.now(), null);

        assertThrows(RuntimeException.class, () -> costCalculator.calculateSingleRentCost(pricing, rent));
    }

    @Test
    void testCalculateTimePeriodEarnings() {
        CostCalculatorImpl costCalculator = new CostCalculatorImpl();
        List<Pricing> pricingList = Arrays.asList(
                createPricing(PaymentStrategy.BY_DAY, BigDecimal.valueOf(10.0), BigDecimal.valueOf(50.0)),
                createPricing(PaymentStrategy.BY_HOUR, BigDecimal.valueOf(10.0), BigDecimal.valueOf(100.0))
        );
        List<Rent> rentList = Arrays.asList(
                createRent(LocalDateTime.now(), LocalDateTime.now().plusDays(2), null),
                createRent(LocalDateTime.now(), LocalDateTime.now().plusHours(5), null)
        );

        BigDecimal earnings = costCalculator.calculateTimePeriodEarnings(pricingList, rentList);

        assertEquals(BigDecimal.valueOf(150.0), earnings);
    }

}
