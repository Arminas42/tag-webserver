package com.kuaprojects.rental.pricing;

import com.kuaprojects.rental.rent.Rent;

import java.math.BigDecimal;
import java.util.List;

public interface CostCalculator {
    BigDecimal calculateSingleRentCost(Pricing pricing, Rent rent);
    BigDecimal calculateTimePeriodEarnings(List<Pricing> locationPricingList, List<Rent> rentList);
}
