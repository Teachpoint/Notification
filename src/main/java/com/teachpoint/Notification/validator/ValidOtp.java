package com.teachpoint.Notification.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidOtpValidator.class)
public @interface ValidOtp {
String message() default   "Cant generate more than 3 times";

Class <?>[] groups() default {};

Class<? extends Payload>[] payload() default{};

}
