package com.fintechharbor.notification.service;

import java.time.Instant;
import com.fintechharbor.notification.requestdto.EmailOtpRequestDto;
import com.fintechharbor.notification.responsedto.EmailOtpResponseDto;

public interface OtpService {

	boolean isOtpExpired(Instant createdTime);

	boolean userAlreadyGeneratedOtp(String email);

	boolean isUserBlocked(String email);

	String encryptOtp(Integer generatedOTP);

    Integer generateOTP(String email);

	void saveOtp(String email, Integer generatedEmailOtp);

	boolean isOtpValid(String email, Integer generatedEmailOtp);

	EmailOtpResponseDto sendOtpToEmail(EmailOtpRequestDto emailOtpRequestDto);

}
