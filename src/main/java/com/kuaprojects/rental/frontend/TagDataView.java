package com.kuaprojects.rental.frontend;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagDataView {
    String tagCode;
    long detectionCount;
}
