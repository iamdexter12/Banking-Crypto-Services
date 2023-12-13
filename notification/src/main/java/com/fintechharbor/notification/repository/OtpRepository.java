package com.fintechharbor.notification.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fintechharbor.notification.model.Otp;

public interface OtpRepository extends MongoRepository<Otp, Integer> {

	Optional<Otp> findByEmail(String email);

}
