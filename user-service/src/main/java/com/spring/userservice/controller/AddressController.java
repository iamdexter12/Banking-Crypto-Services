package com.spring.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.userservice.requestdto.AddressRequestDto;
import com.spring.userservice.service.AddressService;
import com.spring.userservice.util.ResponseHandler;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {
	
	private final AddressService addressService;
	
	@PostMapping("/{email}")
	public ResponseEntity<Object> addAddress(@RequestBody AddressRequestDto addressRequestDto,
			@PathVariable String email) {
		String responseMessage = addressService.addAddress(addressRequestDto, email);
		return ResponseHandler.generateResponse(responseMessage, HttpStatus.OK);
	}


}
