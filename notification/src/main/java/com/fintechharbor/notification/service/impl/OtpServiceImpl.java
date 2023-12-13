package com.fintechharbor.notification.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.fintechharbor.notification.exception.NotFoundException;
import com.fintechharbor.notification.model.Otp;
import com.fintechharbor.notification.repository.OtpRepository;
import com.fintechharbor.notification.requestdto.EmailOtpRequestDto;
import com.fintechharbor.notification.responsedto.EmailOtpResponseDto;
import com.fintechharbor.notification.service.JavamailService;
import com.fintechharbor.notification.service.OtpService;
import com.fintechharbor.notification.util.ExceptionConstant;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OtpServiceImpl implements OtpService {

	private final OtpRepository otpRepository;
	private final JavamailService javamailService;

	private static final int OTP_LENGTH = 6;

	@Override
	public void saveOtp(String email, Integer generatedOTP) {
		otpRepository.findByEmail(email).ifPresentOrElse(existingOtp -> {
			// Update existing user's OTP details
			existingOtp.setGeneratedOtp(encryptOtp(generatedOTP));
			existingOtp.setExpired(false);
			existingOtp.setFailedAttempts(0);
			existingOtp.setCreatedTime(Instant.now());
			otpRepository.save(existingOtp);
		}, () -> {
			// Create a new OTP entry for the user
			Otp otp = new Otp();
			otp.setEmail(email);
			otp.setExpired(false);
			otp.setGeneratedOtp(encryptOtp(generatedOTP));
			otp.setCreatedTime(Instant.now());
			otp.setBlocked(false);
			otp.setFailedAttempts(0);
			otpRepository.save(otp);
		});
	}

	@Override
	public boolean isOtpExpired(Instant createdTime) {
		Instant now = Instant.now();
		Duration duration = Duration.between(createdTime, now);

		// Convert the duration to seconds
		long differenceInSeconds = duration.getSeconds();

		return differenceInSeconds > 300; // OTP expires after 300 seconds (5 minutes)
	}

	@Override
	public boolean userAlreadyGeneratedOtp(String email) {
		return otpRepository.findByEmail(email).map(otp -> {
			Instant now = Instant.now();
			System.out.println("Current timr"+now);
			System.out.println("Time in db"+otp.getCreatedTime());
			Duration duration = Duration.between(otp.getCreatedTime(), now);
			return duration.getSeconds() < 120;
		}).orElse(false);
	}

	@Override
	public boolean isUserBlocked(String email) {
		return otpRepository.findByEmail(email).map(Otp::isBlocked).orElse(false);
	}

	@Override
	public Integer generateOTP(String email) {
		System.out.println("hello before");

		if (isUserBlocked(email)) {
			throw new NotFoundException(ExceptionConstant.FIELD_EMAIL, ExceptionConstant.MESSAGE_ACCOUNT_BLOCKED);
		}
		if (userAlreadyGeneratedOtp(email)) {
			System.out.println("hello");
			throw new NotFoundException(ExceptionConstant.FIELD_EMAIL, ExceptionConstant.MESSAGE_WAIT_FOR_OTP);
		}
		SecureRandom random = new SecureRandom();
		Integer newlygeneratedOtp = random.ints(OTP_LENGTH, 0, 10).reduce(0, (otp, digit) -> otp * 10 + digit);
		saveOtp(email, newlygeneratedOtp);
		return newlygeneratedOtp;
	}

	@Override
	public String encryptOtp(Integer generatedOTP) {
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
	public boolean isOtpValid(String email, Integer emailOtp) {
		String encryptedEnteredOtp = encryptOtp(emailOtp);
		boolean isOtpValid;
		Otp user = otpRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email", "Not found"));

		if (user.isBlocked()) {
			throw new NotFoundException(ExceptionConstant.FIELD_OTP, ExceptionConstant.MESSAGE_ACCOUNT_BLOCKED);
		}

		int failedAttempts = user.getFailedAttempts();

		if (failedAttempts < 3) {
			if (isOtpExpired(user.getCreatedTime())) {
				user.setExpired(true);
				otpRepository.save(user);
				throw new NotFoundException(ExceptionConstant.FIELD_OTP, ExceptionConstant.MESSAGE_EXPIRED_OTP);
			}

			failedAttempts++;

			if (!encryptedEnteredOtp.equals(user.getGeneratedOtp())) {
				user.setFailedAttempts(failedAttempts);
				otpRepository.save(user);
				throw new NotFoundException(ExceptionConstant.FIELD_OTP, ExceptionConstant.MESSAGE_INCORRECT_OTP
						+ (3 - failedAttempts) + ExceptionConstant.MESSAGE_ATTEMPTS_REMAINING);
			} else {
				isOtpValid = true;
				user.setFailedAttempts(0);
				otpRepository.save(user);
			}
		} else {
			user.setBlocked(true);
			otpRepository.save(user);
			throw new NotFoundException(ExceptionConstant.FIELD_EMAIL, ExceptionConstant.MESSAGE_ACCOUNT_BLOCKED);
		}

		return isOtpValid;
	}

	@Override
	public EmailOtpResponseDto sendOtpToEmail(EmailOtpRequestDto emailOtpRequestDto) {
		boolean isEmailSent;
		Integer generatedOTP = generateOTP(emailOtpRequestDto.email());
		try {
			javamailService.sendEmail(emailOtpRequestDto.email(), emailOtpRequestDto.subject(), generatedOTP);
			isEmailSent = true;
		} catch (MessagingException e) {
			throw new NotFoundException(ExceptionConstant.FIELD_EMAIL,
					ExceptionConstant.MESSAGE_SENDING_OTP_ERROR + e.getMessage());
		}
		EmailOtpResponseDto emailOtpResponseDto = new EmailOtpResponseDto(emailOtpRequestDto.email(), isEmailSent);
		return emailOtpResponseDto;
	}

}
