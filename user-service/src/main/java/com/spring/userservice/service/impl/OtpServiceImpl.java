package com.spring.userservice.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import org.springframework.stereotype.Service;
import com.spring.userservice.exception.AlreadyExistException;
import com.spring.userservice.exception.DeactivatedAccountException;
import com.spring.userservice.exception.ExpiredException;
import com.spring.userservice.exception.NotFoundException;
import com.spring.userservice.exception.UserBlockedException;
import com.spring.userservice.model.Otp;
import com.spring.userservice.repository.OtpRepository;
import com.spring.userservice.requestdto.VerifySignupOtpRequestDto;
import com.spring.userservice.service.OtpService;
import com.spring.userservice.util.TokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

	private final OtpRepository otpRepository;
	
	private static final int OTP_LENGTH = 6;

	@Override
	public void saveOtp(String email, Integer generatedOTP, String dataToEncrypt) {
		otpRepository.findByEmail(email).ifPresentOrElse(existingOtp -> {
			// Update existing user's OTP details
			existingOtp.setGeneratedOtp(encryptOtp(generatedOTP));
			existingOtp.setExpired(false);
			existingOtp.setFailedAttempts(0);
			existingOtp.setCreatedTime(new Timestamp(System.currentTimeMillis()));
			otpRepository.save(existingOtp);
		}, () -> {
			// Create a new OTP entry for the user
			Otp otp = new Otp();
			otp.setEmail(email);
			otp.setExpired(false);
			otp.setGeneratedOtp(encryptOtp(generatedOTP));
			otp.setCreatedTime(new Timestamp(System.currentTimeMillis()));
			otp.setBlocked(false);
			otp.setFailedAttempts(0);
			otp.setUserEncryptDetails(dataToEncrypt);
			otpRepository.save(otp);
		});
	}

	@Override
	public boolean isOtpExpired(Timestamp createdTime) {
		long differenceInSeconds = (System.currentTimeMillis() - createdTime.getTime()) / 1000;
		return differenceInSeconds > 300; // OTP expires after 300 seconds (5 minutes)
	}

	@Override
	public boolean userAlreadyGeneratedOtp(String email) {
		return otpRepository.findByEmail(email)
				.map(otp -> (System.currentTimeMillis() - otp.getCreatedTime().getTime()) / 1000 < 30).orElse(false);
	}

	@Override
	public boolean isUserBlocked(String email) {
		return otpRepository.findByEmail(email).map(Otp::isBlocked).orElse(false);
	}

	@Override
	public Integer generateOTP() {
		SecureRandom random = new SecureRandom();
		return random.ints(OTP_LENGTH, 0, 10).reduce(0, (otp, digit) -> otp * 10 + digit);
	}

	@Override
	public String encryptOtp(int generatedOTP) {
		try {
			String otpString = String.valueOf(generatedOTP);
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashedBytes = digest.digest(otpString.getBytes(StandardCharsets.UTF_8));

			return bytesToHex(hashedBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error encrypting OTP", e);
		}
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder();
		for (byte b : bytes) {
			stringBuilder.append(String.format("%02x", b));
		}
		return stringBuilder.toString();
	}

	@Override
	public String[] isOtpValid(VerifySignupOtpRequestDto verifySignupOtpRequestDto, String email) {
		String encryptedEnteredOtp = encryptOtp(verifySignupOtpRequestDto.getEmailOtp());

		Otp user = otpRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email", "Not found"));

		String decryptedUserData = TokenService.decryptData(user.getUserEncryptDetails());
		String[] userDataParts = decryptedUserData.split(",");
		String userEmail = userDataParts[1];

		if (!userEmail.equals(email)) {
			throw new AlreadyExistException("email", "Email doesn't match");
		}

		if (user.isBlocked()) {
			throw new UserBlockedException("Otp", "Account Deactivated");
		}

		int failedAttempts = user.getFailedAttempts();

		if (failedAttempts < 3) {
			if (isOtpExpired(user.getCreatedTime())) {
				user.setExpired(true);
				otpRepository.save(user);
				throw new ExpiredException("Otp", "Expired Otp");
			}

			failedAttempts++;

			if (!encryptedEnteredOtp.equals(user.getGeneratedOtp())) {
				user.setFailedAttempts(failedAttempts);
				otpRepository.save(user);
				throw new NotFoundException("Otp", "Incorrect Otp" + (3 - failedAttempts));
			} else {
				user.setFailedAttempts(0);
				otpRepository.save(user);
			}
		} else {
			user.setBlocked(true);
			otpRepository.save(user);
			throw new DeactivatedAccountException("Email", "Account Deactivated");
		}

		return userDataParts;
	}
}
