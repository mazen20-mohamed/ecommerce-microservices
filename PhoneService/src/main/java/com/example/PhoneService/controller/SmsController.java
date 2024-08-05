package com.example.PhoneService.controller;


import com.example.PhoneService.service.OTPService;
import com.example.PhoneService.service.SMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/sms")
public class SmsController {
    private final SMSService service;
    private final OTPService otpService;

    @PostMapping("/send")
    public String sendSMS(@RequestParam String toNumber, @RequestParam String message) {
        service.sendSMS(toNumber, message);
        return "SMS sent to " + toNumber;
    }


    @PostMapping("/generateOTP")
    public String generateOTP(@RequestParam String mobileNumber){
        return otpService.generateOTP(mobileNumber);
    }

    @GetMapping("/verify")
    public boolean verifyOTP(@RequestParam String mobileNumber,
                             @RequestParam String otp){
        return otpService.verifyOTP(mobileNumber,otp);
    }

}
