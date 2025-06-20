package com.kuaprojects.rental.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagDataViewModel {
    String tagCode;
    long detectionCount;
}
