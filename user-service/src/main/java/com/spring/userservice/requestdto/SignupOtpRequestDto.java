package com.spring.userservice.requestdto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupOtpRequestDto {

	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String mobileNumber;

	@Column(nullable = false)
	private String password;

}
