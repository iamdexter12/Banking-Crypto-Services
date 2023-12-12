package com.spring.userservice.service.impl;

import java.util.UUID;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.spring.userservice.exception.AlreadyExistException;
import com.spring.userservice.exception.ExpiredException;
import com.spring.userservice.exception.NotFoundException;
import com.spring.userservice.model.User;
import com.spring.userservice.repository.UserRepository;
import com.spring.userservice.requestdto.KycRequestDto;
import com.spring.userservice.requestdto.SignupOtpRequestDto;
import com.spring.userservice.requestdto.VerifySignupOtpRequestDto;
import com.spring.userservice.service.JavamailService;
import com.spring.userservice.service.OtpService;
import com.spring.userservice.service.UserService;
import com.spring.userservice.util.TokenService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final OtpService otpService;
	private final JavamailService javamailService;
	private final UserRepository userRepository;

	@Override
	public String sendSignupOtp(SignupOtpRequestDto signupOtpRequestDto) {
		getUser(signupOtpRequestDto.getEmail()).ifPresent(user -> {
			throw new AlreadyExistException("Email", "Already exist");
		});

		if (otpService.isUserBlocked(signupOtpRequestDto.getEmail())) {
			throw new ExpiredException("Email", "Blocked");
		}

		if (otpService.userAlreadyGeneratedOtp(signupOtpRequestDto.getEmail())) {
			throw new AlreadyExistException("OTP", "Wait for 30 seconds");
		}
		Integer generatedEmailOtp = otpService.generateOTP();
		String dataToEncrypt = UUID.randomUUID().toString() + "," + signupOtpRequestDto.getEmail() + ","
				+ signupOtpRequestDto.getMobileNumber() + "," + signupOtpRequestDto.getName() + ","
				+ signupOtpRequestDto.getPassword();

		String encryptedUserData = TokenService.encryptData(dataToEncrypt);
		otpService.saveOtp(signupOtpRequestDto.getEmail(), generatedEmailOtp, encryptedUserData);

		try {
			javamailService.sendEmail(signupOtpRequestDto.getEmail(), "Verify Email", generatedEmailOtp,
					signupOtpRequestDto.getEmail());
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return encryptedUserData;
	}

	@Override
	public String registerUser(String email, VerifySignupOtpRequestDto verifySignupOtpRequestDto) {
		String[] userDataParts = otpService.isOtpValid(verifySignupOtpRequestDto, email);

		User createUserFromUserDataParts = createUserFromUserDataParts(userDataParts);

		getUser(createUserFromUserDataParts.getEmail())
				.ifPresent(user -> new AlreadyExistException(email, "Already registered"));

		userRepository.save(createUserFromUserDataParts);

		return "Registered";
	}

	private User createUserFromUserDataParts(String[] userDataParts) {
		User user = new User();
		user.setEmail(userDataParts[1]);
		user.setName(userDataParts[3]);
		user.setMobileNumber(userDataParts[2]);
		user.setPassword(userDataParts[4]);
		user.setKycStatus("Not Submitted");
		return user;
	}

	@Override
	public String completeKyc(KycRequestDto kycRequestDto, String email) {
		Optional<User> user=userRepository.findByEmail(email);
		if(user.isEmpty()) {
			throw new NotFoundException(email, "Email donot exists");
		}
		User currentUser=user.get();
		if(currentUser.getKycStatus().equals("Approved")) {
			throw new AlreadyExistException(email, "Kyc is approved");
		}
		currentUser.setAadharCard(kycRequestDto.getAadharCard());
		currentUser.setPancard(kycRequestDto.getPanCard());
		userRepository.save(currentUser);
		return"Kyc completed";
	}

	@Override
	public Optional<User> getUser(String email) {
		return userRepository.findByEmail(email);
	}
}
