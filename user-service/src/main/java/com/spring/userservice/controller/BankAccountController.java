package com.spring.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.userservice.requestdto.AddbankAccountRequestDto;
import com.spring.userservice.service.BankService;
import com.spring.userservice.util.ResponseHandler;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank")
public class BankAccountController {

	private final BankService bankService;

	@PostMapping("/{email}")
	public ResponseEntity<Object> addBankAccount(@RequestBody AddbankAccountRequestDto addbankAccountRequestDto,
			@PathVariable String email) {
		String openBankAccount = bankService.openBankAccount(addbankAccountRequestDto, email);

		return ResponseHandler.generateResponse(openBankAccount, HttpStatus.CREATED);
	}

}
