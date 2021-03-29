package com.teachpoint.Notification.validator;

import com.teachpoint.Notification.entity.Otp;
import com.teachpoint.Notification.repository.OtpRepository;
import com.teachpoint.Notification.service.OtpService;
import com.teachpoint.Notification.service.OtpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

public class ValidOtpValidator implements ConstraintValidator<ValidOtp, Otp> {


    private OtpService otpService;

    public ValidOtpValidator(OtpService otpService) {
        System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
        this.otpService = otpService;
    }

    @Override
    public boolean isValid(Otp otp, ConstraintValidatorContext constraintValidatorContext) {

    Optional<List<Otp>> otpOptional = otpService.getOtpsByObjectIdAndExpirationDateGreate(otp.getObjectId());
        if (otpOptional.isEmpty()) {
            System.out.println("not eny otp for "+otp.getObjectId());
            return true;
        } else {
            List<Otp> otpList = otpOptional.get();

            System.out.println("There are "+otpList.size()+" otps for "+otp.getObjectId());
            if (otpList.size() < 3) {
                return true;
            }
        }

        return false;
    }
}
