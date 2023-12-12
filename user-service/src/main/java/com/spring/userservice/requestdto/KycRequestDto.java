package com.spring.userservice.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KycRequestDto {

	private String aadharCard;
	private String panCard;

}
