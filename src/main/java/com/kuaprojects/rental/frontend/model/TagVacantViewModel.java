package com.kuaprojects.rental.frontend.model;

import com.kuaprojects.rental.tag.ProcessingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TagVacantViewModel {

    String tagCode;
    private Long elapsedMinutes;
    private ProcessingStatus status;
}
