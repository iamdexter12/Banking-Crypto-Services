package com.spring.userservice.service;

import com.spring.userservice.requestdto.AddressRequestDto;

public interface AddressService {

	String addAddress(AddressRequestDto addressRequestDto, String email);

}
