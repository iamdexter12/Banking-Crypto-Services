package com.spring.userservice.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.spring.userservice.exception.AlreadyExistException;
import com.spring.userservice.exception.NotFoundException;
import com.spring.userservice.model.Address;
import com.spring.userservice.model.User;
import com.spring.userservice.repository.AddressRepository;
import com.spring.userservice.repository.UserRepository;
import com.spring.userservice.requestdto.AddressRequestDto;
import com.spring.userservice.service.AddressService;
import com.spring.userservice.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

	private final UserService userService;
	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	@Override
	public String addAddress(AddressRequestDto addressRequestDto, String email) {
		Optional<User> user = userService.getUser(email);
		if (user.isEmpty()) {
			throw new NotFoundException("email", "Not found");
		}
		if (Objects.nonNull(user.get().getAddress())) {
			throw new AlreadyExistException("Address", "Already added");
		}
		Address address = new Address();
		address.setPermanentAddress(addressRequestDto.getPermanentAddress());
		address.setState(addressRequestDto.getState());
		address.setCity(addressRequestDto.getCity());
		address.setPinCode(addressRequestDto.getPinCode());
		address.setUser(user.get());
		addressRepository.save(address);
		user.get().setAddress(address);
		userRepository.save(user.get());
		return "Address Added successfully";
	}

}
