package com.teachpoint.Notification.repository;

import com.teachpoint.Notification.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Integer> {

    Optional<Otp> findByObjectIdAndStatusAndExpirationDateGreaterThan(String objectId, int status, Instant expirationDate);

    Optional<Otp> findByObjectId(String objectId);

    Optional<List<Otp>> findAllByObjectIdAndEventDateGreaterThan(String objectId, Instant eventDate);
}
