package com.kuaprojects.rental.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    List<ApiKey> findByScope(String scope);

}
