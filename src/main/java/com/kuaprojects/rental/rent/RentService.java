package com.kuaprojects.rental.rent;

import java.util.List;

public interface RentService {
    Rent createRent(Long trailerId, String driverCarLicensePlate, String driverFullName);
    List<Rent> getAllRent();
    Rent findRent(Long id);
    Rent endRent(Long id);
}
