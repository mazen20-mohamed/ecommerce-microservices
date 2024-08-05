package com.example.PhoneService.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPService {
    private final SMSService smsService;
    private final OTPStorageService otpStorageService;
    public String generateOTP(String mobileNumber){
        String otp = String.format("%04d", new Random().nextInt(10000));
        otpStorageService.storeOTP(mobileNumber,otp);
        smsService.sendSMS(mobileNumber,"Your OTP is: " + otp);
        return otp;
    }

    public boolean verifyOTP(String mobileNumber, String otp) {
        String storedOtp = otpStorageService.getOTP(mobileNumber);
        return otp.equals(storedOtp);
    }

}
