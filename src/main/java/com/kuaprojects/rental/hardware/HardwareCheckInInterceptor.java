package com.kuaprojects.rental.hardware;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class HardwareCheckInInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(HardwareCheckInInterceptor.class);
    private final HardwareService hardwareService;

    public HardwareCheckInInterceptor(HardwareService hardwareService) {
        this.hardwareService = hardwareService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String hardwareId = request.getHeader("X-Hardware-ID");
        if (hardwareId != null) {
            hardwareService.updateLastCheckedIn(hardwareId, LocalDateTime.now());
            logger.info("Tried to update last check in of hardware.");
        }else {
            logger.info("No 'X-Hardware-ID' header!");
        }
        return true; // Continue with the request
    }
}

