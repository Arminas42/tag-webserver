package com.kuaprojects.rental.unit;

import com.kuaprojects.rental.pricing.CalculationException;
import com.kuaprojects.rental.pricing.CostCalculatorImpl;
import com.kuaprojects.rental.pricing.PaymentStrategy;
import com.kuaprojects.rental.pricing.Pricing;
import com.kuaprojects.rental.rent.Rent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.kuaprojects.rental.TestUtil.createPricing;
import static com.kuaprojects.rental.TestUtil.createRent;
import static com.kuaprojects.rental.TestUtil.trailer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CostCalculatorTest {


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

    @Test
    void testCalculationDefaultsToOneHour_IfOnlyMinutesElapsed() {
        CostCalculatorImpl costCalculator = new CostCalculatorImpl();
        List<Pricing> pricingList = List.of(
                createPricing(PaymentStrategy.BY_HOUR, BigDecimal.valueOf(10.0), BigDecimal.valueOf(100.0))
        );
        List<Rent> rentList = List.of(
                createRent(LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), null)
        );

        BigDecimal earnings = costCalculator.calculateTimePeriodEarnings(pricingList, rentList);
        assertEquals(BigDecimal.valueOf(10.0), earnings);
    }

    @Test
    void testCalculationDefaultsToLowerBound() {
        CostCalculatorImpl costCalculator = new CostCalculatorImpl();
        List<Pricing> pricingList = List.of(
                createPricing(PaymentStrategy.BY_HOUR, BigDecimal.valueOf(10.0), BigDecimal.valueOf(100.0))
        );
        List<Rent> rentList = List.of(
                createRent(LocalDateTime.now(), LocalDateTime.now().plusHours(1).plusMinutes(25), trailer)
        );

        BigDecimal earnings = costCalculator.calculateTimePeriodEarnings(pricingList, rentList);
        assertEquals(BigDecimal.valueOf(10.0), earnings);
    }

    @Test
    void testCalculationThrowsException_WhenMultiplePricingsExistOfTheSameType() {
        CostCalculatorImpl costCalculator = new CostCalculatorImpl();
        List<Pricing> pricingList = Arrays.asList(
                createPricing(PaymentStrategy.BY_DAY, BigDecimal.valueOf(10.0), BigDecimal.valueOf(50.0)),
                createPricing(PaymentStrategy.BY_HOUR, BigDecimal.valueOf(10.0), BigDecimal.valueOf(100.0))
        );
        List<Rent> rentList = Arrays.asList(
                createRent(LocalDateTime.now(), LocalDateTime.now().plusDays(2), null),
                createRent(LocalDateTime.now(), LocalDateTime.now().plusHours(5), null)
        );

        Exception exception =
                assertThrows(CalculationException.class,
                        () -> costCalculator.calculateTimePeriodEarnings(pricingList, rentList));
        assertEquals("Multiple pricings for the same trailer type", exception.getMessage());
    }
}
