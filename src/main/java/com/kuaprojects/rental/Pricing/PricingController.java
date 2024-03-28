package com.kuaprojects.rental.Pricing;

import com.kuaprojects.rental.Location.ParkLocation;
import com.kuaprojects.rental.Trailer.TrailerType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
public class PricingController {
    private final PricingRepository pricingRepository;
    private final PricingService pricingService;

    public PricingController(PricingRepository pricingRepository, PricingService pricingService) {
        this.pricingRepository = pricingRepository;
        this.pricingService = pricingService;
    }

    @GetMapping("pricing")
    List<Pricing> getAllPricing() {
        return Collections.emptyList();
    }

    @GetMapping("pricing/{id}")
    Pricing getPricingById(@PathVariable Long id) {
        return pricingRepository.findById(id).orElseThrow(() -> new PricingNotFoundException(id));
    }

    @PostMapping("pricing")
    Pricing createPricing(@RequestBody PricingDTO requestBody) {
        var pricing = Pricing.builder()
                .costByDay(BigDecimal.valueOf(requestBody.getCostByDay()))
                .costByHour(BigDecimal.valueOf(requestBody.getCostByHour()))
                .paymentStrategy(PaymentStrategy.valueOf(requestBody.getPaymentStrategy()))
                .trailerParkLocation(ParkLocation.PANEVEZYS)
                .trailerType(TrailerType.valueOf(requestBody.getTypeOfTrailer()))
                .build();
        return pricingRepository.save(pricing);
    }

    @PostMapping("pricing/earnings")
    BigDecimal getEarnings(@RequestBody EarningsPeriodDTO earningsPeriodDTO) {
        return pricingService.getPeriodEarning(earningsPeriodDTO.from, earningsPeriodDTO.to);
    }
}
