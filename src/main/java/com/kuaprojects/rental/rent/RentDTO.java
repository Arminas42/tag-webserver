package com.kuaprojects.rental.rent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class RentDTO {
    private String driverCarLicensePlate;
    private String driverFullName;
    private Long rentedTrailerId;
}
