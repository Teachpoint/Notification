package com.teachpoint.Notification.service;

import com.teachpoint.Notification.dto.OtpValidateResponse;
import com.teachpoint.Notification.entity.Otp;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface OtpService {

    ResponseEntity<OtpValidateResponse> generateOTP(String key);
    Optional<List<Otp>> getOtpsByObjectIdAndExpirationDateGreate(String objectId);
}
