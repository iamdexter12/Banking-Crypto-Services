package com.spring.userservice.service;

import com.spring.userservice.requestdto.AddbankAccountRequestDto;

public interface BankService {

	String openBankAccount(AddbankAccountRequestDto addbankAccountRequestDto, String email);

}
