package com.kuaprojects.rental.frontend;

import com.kuaprojects.rental.tag.ProcessingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TagVacantView {

    String tagCode;
    private Long elapsedMinutes;
    private ProcessingStatus status;
}
