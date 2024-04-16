package com.kuaprojects.rental.pricing;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EarningsPeriodDTO {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime from;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime to;
}
