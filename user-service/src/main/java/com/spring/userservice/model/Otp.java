package com.spring.userservice.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Otp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String generatedOtp;
	private Timestamp createdTime;
	private String email;
	private boolean isExpired;
	private boolean isBlocked;
	private int failedAttempts;
	private String userEncryptDetails;

}
