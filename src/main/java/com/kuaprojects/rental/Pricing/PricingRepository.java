package com.kuaprojects.rental.Pricing;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricingRepository extends JpaRepository<Pricing, Long> {

    @Override
    @Cacheable("pricingList")
    List<Pricing> findAll();
}
