package com.teachpoint.Notification.service;


import com.teachpoint.Notification.dto.OtpValidateResponse;
import com.teachpoint.Notification.entity.Otp;
import com.teachpoint.Notification.repository.OtpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    @Value("${otp.timeout: default 2}")
    private Integer EXPIRE_MINS;
    @Value("${otp.generatingCount: default 3}")
    private Integer OTP_COUNT;
    @Value("${otp.generatingperiod: default 24}")
    private Integer OTP_PERIOD;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private OtpRepository otpRepository;

    public OtpServiceImpl(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public ResponseEntity<OtpValidateResponse> generateOTP(String studentId) {
        System.out.println("Generating ");

        Optional<List<Otp>> optionalOtpList = getOtpsByObjectIdAndExpirationDateGreate(studentId);

        if (!optionalOtpList.isEmpty() && optionalOtpList.get().size() >= OTP_COUNT) {
            System.out.println(optionalOtpList.get().size());
            logger.error(studentId + "=> Your otp generation limit exceeded. Please try again tomorrow");
            return ResponseEntity.badRequest().body(new OtpValidateResponse(-400, "Your otp generation limit exceeded. Please try again tomorrow"));
        }

        Optional<Otp> currentOptionalOtp = otpRepository.findByObjectIdAndStatusAndExpirationDateGreaterThan(studentId, 0, Instant.now());

        if (!currentOptionalOtp.isEmpty()) {
            logger.error(studentId + "=> Can't generate new otp, please use current");
            return ResponseEntity.badRequest().body(new OtpValidateResponse(-400, "Can't generate new otp, please use current"));
        }

        Random random = new Random();
        int otpCode = 100000 + random.nextInt(900000);
        Instant expDate = Instant.now().plus(EXPIRE_MINS, ChronoUnit.MINUTES);
        Instant currDate = Instant.now();
        Otp otp = new Otp(studentId, otpCode, 0, currDate, expDate);
        System.out.println(otp.toString());
        otpRepository.save(otp);
        logger.info(studentId + ": Generated otp code " + otp);
        return ResponseEntity.ok().body(new OtpValidateResponse(200, "GENERATED"));
    }


    public ResponseEntity<OtpValidateResponse> validateOtp(String objectId, Long otpCode){
       Optional<Otp> serverOtpOptional =  otpRepository.findByObjectIdAndStatusAndExpirationDateGreaterThan(objectId, 0, Instant.now());

       if (serverOtpOptional.isEmpty()){
           return ResponseEntity.badRequest().body(new OtpValidateResponse(-400, "Otp code expired. Please generate new one"));
       }
        Otp serverOtp = serverOtpOptional.get();
       if (otpCode == serverOtp.getOtpCode()) {
            System.out.println("SUCESSSS");
            serverOtp.setStatus(1);
            otpRepository.save(serverOtp);
            return ResponseEntity.ok().body(new OtpValidateResponse(200, "Valid OTP code"));
        } else {
            return ResponseEntity.badRequest().body(new OtpValidateResponse(-400, "Wrong OTP code. Please try again."));
        }

    }

//    public Otp getOtp(String key) {
//        System.out.println(Instant.now());
//        Optional<Otp> otpOptional = otpRepository.findByObjectIdAndStatusAndExpirationDateGreaterThan(key, 0, Instant.now());
//
//        if (otpOptional.isEmpty()) {
//            return 0;
//        } else {
//            System.out.println("not empty " + otpOptional.get().toString());
//            return otpOptional.get().getOtpCode();
//        }
//
//    }

    public Optional<List<Otp>> getOtpsByObjectIdAndExpirationDateGreate(String objectId) {

        Optional<List<Otp>> optionalOtpList = otpRepository.findAllByObjectIdAndEventDateGreaterThan(objectId, Instant.now().plus(-1 * OTP_PERIOD, ChronoUnit.HOURS));
        System.out.println("Optional<List<Otp>> " + optionalOtpList);
        return optionalOtpList;
    }

    public void clearOTP(String key) {
        Optional<Otp> otpOptional = otpRepository.findByObjectId(key);

        if (otpOptional.isEmpty()) {
            return;
        }
        otpRepository.delete(otpOptional.get());
    }
}