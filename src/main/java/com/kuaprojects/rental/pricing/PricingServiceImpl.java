package com.kuaprojects.rental.pricing;

import com.kuaprojects.rental.rent.Rent;
import com.kuaprojects.rental.rent.RentRepository;
import com.kuaprojects.rental.rent.RentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PricingServiceImpl implements PricingService {

    private static final Logger log = LoggerFactory.getLogger(PricingServiceImpl.class);

    PricingRepository pricingRepository;
    RentService rentService;
    CostCalculator costCalculator;
    RentRepository rentRepository;

    public PricingServiceImpl(PricingRepository pricingRepository, RentService rentService, CostCalculator costCalculator, RentRepository rentRepository) {
        this.pricingRepository = pricingRepository;
        this.rentService = rentService;
        this.costCalculator = costCalculator;
        this.rentRepository = rentRepository;
    }


    @Override
    public BigDecimal getRentCost(Long rentId) {
        var rent = rentService.findRent(rentId);
        if (rent.getRentEndTimestamp() == null) {
            log.warn("Rent has not ended");
            return BigDecimal.ZERO;
        }
        var pricingList = pricingRepository.findAll();
        var pricingRelatedToRent = pricingList.stream()
                .filter(pricing -> pricing.getTrailerType().equals(rent.getRentedTrailer().getType()))
                .findFirst().orElseThrow(() -> new RuntimeException("Missing pricing for trailer type"));

        return costCalculator.calculateSingleRentCost(pricingRelatedToRent, rent);
    }

    @Override
    public BigDecimal getPeriodEarning(LocalDateTime fromTimestamp, LocalDateTime toTimestamp) {
        List<Rent> endedRentListOfPeriod = rentRepository.findAllByRentEndTimestampBetween(fromTimestamp, toTimestamp);
        List<Pricing> pricingList = pricingRepository.findAll();
        return costCalculator.calculateTimePeriodEarnings(pricingList, endedRentListOfPeriod);
    }
}
