package com.teachpoint.Notification.controller;


import com.teachpoint.Notification.dto.OtpValidateResponse;
import com.teachpoint.Notification.entity.Otp;
import com.teachpoint.Notification.service.EmailService;
import com.teachpoint.Notification.service.OtpServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class OTPController {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private OtpServiceImpl otpServiceImpl;
    private RestTemplate restTemplate;
    private EmailService emailService;

    public OTPController(OtpServiceImpl otpServiceImpl, RestTemplate restTemplate, EmailService emailService) {
        this.otpServiceImpl = otpServiceImpl;
        this.restTemplate = restTemplate;
        this.emailService = emailService;
    }

    @GetMapping("/generateOTP/{studentId}")
    public ResponseEntity<OtpValidateResponse> generateOTP(@PathVariable String studentId) throws MessagingException {

        ResponseEntity<OtpValidateResponse> otpValidateResponseResponseEntity = otpServiceImpl.generateOTP(studentId);
        System.out.println(otpValidateResponseResponseEntity);
//        emailService.sendMessage("sgulmammadov@gmail.com", "Teachpoint activation code", "Activation code is: "+otp);
        return otpValidateResponseResponseEntity;
    }

    @GetMapping("/validateOtp/{objectId}/")
    public ResponseEntity<OtpValidateResponse> validateOTP(@PathVariable String objectId, @RequestParam Long otpnum) {

        OtpValidateResponse otpValidateResponse;
        if (otpnum > 1) {
            logger.info("external otp " + otpnum);
            return otpServiceImpl.validateOtp(objectId, otpnum);
        } else {
            otpValidateResponse = new OtpValidateResponse(-400, "Invalid OTP code");
            return new ResponseEntity<>(otpValidateResponse, HttpStatus.BAD_REQUEST);
        }


    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<OtpValidateResponse> HandleOtpFailure(MethodArgumentNotValidException ex) {
//        String errorDsc = ex.getBindingResult().getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(", "));
//        return ResponseEntity.badRequest().body(new OtpValidateResponse(-400, errorDsc));
//    }

}
