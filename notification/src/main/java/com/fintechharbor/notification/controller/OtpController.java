package com.fintechharbor.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fintechharbor.notification.requestdto.EmailOtpRequestDto;
import com.fintechharbor.notification.responsedto.EmailOtpResponseDto;
import com.fintechharbor.notification.service.OtpService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {

	private final OtpService otpService;

	@PostMapping("/send/email")
	public ResponseEntity<EmailOtpResponseDto> sendOtpToEmail(@RequestBody EmailOtpRequestDto emailOtpRequestDto) {
		return ResponseEntity.ok(otpService.sendOtpToEmail(emailOtpRequestDto));
	}

}
