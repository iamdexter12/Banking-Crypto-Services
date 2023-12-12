package com.spring.userservice.service;

import java.sql.Timestamp;
import com.spring.userservice.requestdto.VerifySignupOtpRequestDto;

public interface OtpService {


	public boolean isOtpExpired(Timestamp createdTime);

	public boolean userAlreadyGeneratedOtp(String email);

	public boolean isUserBlocked(String email);

	public String encryptOtp(int generatedOTP);

	public Integer generateOTP();

	public void saveOtp(String email, Integer generatedEmailOtp, String dataToEncrypt);

	public String[] isOtpValid(VerifySignupOtpRequestDto verifySignupOtpRequestDto, String email);
	

}
