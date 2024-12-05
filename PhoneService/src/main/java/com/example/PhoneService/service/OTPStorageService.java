package com.example.PhoneService.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class OTPStorageService {

    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();

    public void storeOTP(String mobileNumber, String otp) {
        otpStorage.put(mobileNumber, otp);
        // Set TTL (Time To Live) for OTP
        new Thread(() -> {
            try {
                TimeUnit.MINUTES.sleep(3); // TTL of 5 minutes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            otpStorage.remove(mobileNumber);
        }).start();
    }

    public String getOTP(String mobileNumber) {
        return otpStorage.get(mobileNumber);
    }
}
