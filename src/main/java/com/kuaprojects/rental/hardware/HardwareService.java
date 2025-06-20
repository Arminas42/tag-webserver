package com.kuaprojects.rental.hardware;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HardwareService {
    private final HardwareRepository hardwareRepository;

    public HardwareService(HardwareRepository hardwareRepository) {
        this.hardwareRepository = hardwareRepository;
    }

    public void updateLastCheckedIn(String hardwareCode, LocalDateTime timeOfCheckingIn){
        var hardware = hardwareRepository.findByHardwareCode(hardwareCode);
        hardware.ifPresent(hardwareObj -> {
            hardwareObj.setLastCheckedIn(timeOfCheckingIn);
            hardwareRepository.save(hardwareObj);
        });
        if (hardware.isEmpty()){
            hardwareRepository.save(Hardware.builder()
                    .name("Raspberry Pi")
                    .location("main")
                    .hardwareCode(hardwareCode)
                    .lastCheckedIn(timeOfCheckingIn)
                    .build());
        }
    }
}
