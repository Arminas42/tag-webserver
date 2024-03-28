package com.kuaprojects.rental.Rent;

public class RentNotFoundException extends RuntimeException {
    public RentNotFoundException(Long id) {
        super("Rent not found: " + id);
    }
}
