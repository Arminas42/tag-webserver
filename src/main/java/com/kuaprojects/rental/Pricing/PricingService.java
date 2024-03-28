package com.kuaprojects.rental.Pricing;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PricingService {
    BigDecimal getRentCost(Long rentId);
    BigDecimal getPeriodEarning(LocalDateTime from, LocalDateTime to);

}
