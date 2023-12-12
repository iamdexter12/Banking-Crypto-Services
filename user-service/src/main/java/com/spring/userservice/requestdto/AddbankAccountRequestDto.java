package com.spring.userservice.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddbankAccountRequestDto {
	
	private String bankName;
	private Double balance;

}
