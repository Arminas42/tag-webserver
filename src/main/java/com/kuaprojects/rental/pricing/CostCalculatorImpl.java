package com.kuaprojects.rental.pricing;

import com.kuaprojects.rental.rent.Rent;
import com.kuaprojects.rental.trailer.TrailerType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;

@Component
public class CostCalculatorImpl implements CostCalculator {

    public static final double DOUBLE_VALUE_OF_HOURS_IN_A_DAY = 24.0;

    @Override
    public BigDecimal calculateSingleRentCost(Pricing pricing, Rent rent) {
        long timeDifference = HOURS.between(rent.getRentStartTimestamp(), rent.getRentEndTimestamp());
        var costByHour = pricing.getCostByHour().doubleValue();
        var costByDay = pricing.getCostByDay().doubleValue();
        switch (pricing.getPaymentStrategy()) {
            case BY_DAY -> {
                return calculateCostByDay(timeDifference, costByDay);
            }
            case BY_HOUR -> {
                double cost = costByHour * timeDifference;
                if (cost > costByDay) {
                    return calculateCostByDay(timeDifference, costByDay);
                }
                return BigDecimal.valueOf(cost);

            }
            default -> throw new CalculationException("Could not calculate rent cost, not existing payment strategy");
        }
    }

    private BigDecimal calculateCostByDay(long timeDifference, double costByDay) {
        var days = Math.ceil(timeDifference / DOUBLE_VALUE_OF_HOURS_IN_A_DAY);
        return BigDecimal.valueOf(days * costByDay);
    }

    @Override
    public BigDecimal calculateTimePeriodEarnings(List<Pricing> locationPricingList, List<Rent> rentList) {
        return rentList.stream()
                .map(rent -> {
                    var pricing = getRentPricingObject(locationPricingList, rent.getRentedTrailer().getType());
                    return calculateSingleRentCost(pricing, rent);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Pricing getRentPricingObject(List<Pricing> locationPricingList, TrailerType trailerType) {
        return locationPricingList.stream()
                .filter(pricing -> pricing.getTrailerType().equals(trailerType))
                .findFirst()
                .orElseThrow(() -> new CalculationException("Could not find appropriate pricing"));
    }
}
