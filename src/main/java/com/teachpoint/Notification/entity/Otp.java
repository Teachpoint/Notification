package com.teachpoint.Notification.entity;

import com.teachpoint.Notification.validator.ValidOtp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="otps")
public class Otp {

    public Otp(String objectId, int otpCode, int status, Instant eventDate, Instant expirationDate) {
        this.objectId = objectId;
        this.otpCode = otpCode;
        this.status = status;
        this.eventDate = eventDate;
        this.expirationDate = expirationDate;
    }

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String objectId;
    private int otpCode;
    private int status;
    private Instant eventDate;
    private Instant expirationDate;
}
