package com.kuaprojects.rental.Trailer;

import lombok.Getter;

@Getter
public enum TrailerType {
    TRAILER_200_CM("Trailer", 200),
    TRAILER_300_CM("Trailer", 300),
    TRAILER_330_CM("Trailer", 330),
    TRAILER_400_CM("Trailer", 400),
    TRAILER_450_CM("Trailer", 450),
    TRAILER_BIG_WALLS("Trailer big walls", 450),
    TRAILER_WITH_TENT_200_CM("Trailer with tent", 200),
    TRAILER_WITH_TENT_300_CM("Trailer with tent", 300),
    TRAILER_WITH_TENT_330_CM("Trailer with tent", 330),
    TRAILER_WITH_TENT_400_CM("Trailer with tent", 400),
    TRAILER_WITH_TENT_DOUBLE_AXIS_400_CM("Trailer with tent, double eaxis", 400),
    TRAWL_SMALL("Small trawl",200),
    TRAWL_BIG("Big trawl",300);


    private final String nameOfType;
    private final int lengthByCentimeters;

    TrailerType(String nameOfType, int lengthByCentimeters) {
        this.nameOfType = nameOfType;
        this.lengthByCentimeters = lengthByCentimeters;
    }
}
