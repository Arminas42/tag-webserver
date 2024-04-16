package com.kuaprojects.rental.pricing;

public class PricingNotFoundException extends RuntimeException {
    public PricingNotFoundException(Long id) {
        super("Pricing not found: " + id);
    }

}
