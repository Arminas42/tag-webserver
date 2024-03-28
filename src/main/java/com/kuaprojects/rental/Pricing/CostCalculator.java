package com.kuaprojects.rental.Pricing;

import com.kuaprojects.rental.Location.ParkLocation;
import com.kuaprojects.rental.Rent.Rent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CostCalculator {
    BigDecimal calculateSingleRentCost(Pricing pricing, Rent rent);
    BigDecimal calculateTimePeriodEarnings(List<Pricing> locationPricingList, List<Rent> rentList);
}
