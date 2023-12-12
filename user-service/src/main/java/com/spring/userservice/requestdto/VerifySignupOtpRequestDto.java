package com.spring.userservice.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifySignupOtpRequestDto {
	
	private Integer emailOtp;

}
