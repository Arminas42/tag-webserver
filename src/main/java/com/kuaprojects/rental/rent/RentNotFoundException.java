package com.kuaprojects.rental.rent;

public class RentNotFoundException extends RuntimeException {
    public RentNotFoundException(Long id) {
        super("Rent not found: " + id);
    }
}
