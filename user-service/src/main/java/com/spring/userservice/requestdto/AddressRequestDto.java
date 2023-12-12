package com.spring.userservice.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDto {
	
	private String permanentAddress;
	private String state;
	private String city;
	private String pinCode;

}
