package com.kuaprojects.rental.Pricing;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EarningsPeriodDTO {
    LocalDateTime from;
    LocalDateTime to;
}
