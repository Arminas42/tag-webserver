package com.kuaprojects.rental.Pricing;

public class PricingNotFoundException extends RuntimeException {
    public PricingNotFoundException(Long id) {
        super("Pricing not found: " + id);
    }

}
