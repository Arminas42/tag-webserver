package com.kuaprojects.rental.configuration;

import com.kuaprojects.rental.hardware.HardwareCheckInInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private HardwareCheckInInterceptor hardwareCheckInInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(hardwareCheckInInterceptor)
                .addPathPatterns("/api/tag/rfid", "/api/tag/rfid/**"); // Apply to specific endpoints
    }
}
