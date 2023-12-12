package com.spring.userservice.service;

import java.util.Optional;

import com.spring.userservice.model.User;
import com.spring.userservice.requestdto.KycRequestDto;
import com.spring.userservice.requestdto.SignupOtpRequestDto;
import com.spring.userservice.requestdto.VerifySignupOtpRequestDto;

public interface UserService {

	String sendSignupOtp(SignupOtpRequestDto signupOtpRequestDto);

	String registerUser(String email, VerifySignupOtpRequestDto verifySignupOtpRequestDto);

	String completeKyc(KycRequestDto kycRequestDto, String email);

	Optional<User> getUser(String email);

}
