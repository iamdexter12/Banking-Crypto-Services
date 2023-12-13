package com.spring.userservice.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.userservice.config.WebclientConfig;
import com.spring.userservice.requestdto.EmailOtpRequestDto;
import com.spring.userservice.requestdto.KycRequestDto;
import com.spring.userservice.requestdto.SignupOtpRequestDto;
import com.spring.userservice.requestdto.VerifySignupOtpRequestDto;
import com.spring.userservice.responsedto.EmailOtpResponseDto;
import com.spring.userservice.service.UserService;
import com.spring.userservice.util.ResponseHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "API controller for all user related operation")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final WebclientConfig webclientConfig;

	@PostMapping("/verify")
	public ResponseEntity<Object> verifyAndRegister(@RequestBody VerifySignupOtpRequestDto verifySignupOtpRequestDto,
			@RequestParam(required = true) String email) {
		String registerUser = userService.registerUser(email, verifySignupOtpRequestDto);

		Link doKycLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).doKyc(null, email))
				.withRel("doKyc");

		Link addAddressLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).addAddress(null, email))
				.withRel("addAddress");
		Link addBankAccountLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(BankAccountController.class).addBankAccount(null, email))
				.withRel("addBankAccount");

		List<Link> links = Arrays.asList(doKycLink, addAddressLink, addBankAccountLink);
		return ResponseHandler.generateResponse(registerUser, HttpStatus.CREATED, links);
	}

	@PostMapping("/create/singup-otp")
	public ResponseEntity<Object> generateSignupOtp(@RequestBody SignupOtpRequestDto signupOtpRequestDto) {

		String sendSignupOtp = userService.sendSignupOtp(signupOtpRequestDto);
		Link verifyEmailOtpLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.verifyAndRegister(null, signupOtpRequestDto.getEmail())).withRel("verifyAndRegister");

		List<Link> links = Arrays.asList(verifyEmailOtpLink);
		return ResponseHandler.generateResponse("Otp Sent succesfully", HttpStatus.CREATED, sendSignupOtp, links);
	}

	@PatchMapping("/kyc/{email}")
	public ResponseEntity<Object> doKyc(@RequestBody KycRequestDto kycRequestDto, @PathVariable String email) {
		String completeKyc = userService.completeKyc(kycRequestDto, email);
		return ResponseHandler.generateResponse(completeKyc, HttpStatus.CREATED);
	}

	@PostMapping("/send/otp")
	public ResponseEntity<EmailOtpResponseDto> demo(@RequestBody EmailOtpRequestDto emailRequestDto) {
		EmailOtpResponseDto post = webclientConfig.post("http://localhost:1010/otp/send/email", emailRequestDto,
				EmailOtpResponseDto.class);
		log.info("Response from OtpService {}", post);
		return ResponseEntity.ok(post);

	}

}
