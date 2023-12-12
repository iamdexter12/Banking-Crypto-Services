package com.spring.userservice.service.impl;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import com.spring.userservice.exception.AlreadyExistException;
import com.spring.userservice.exception.NotFoundException;
import com.spring.userservice.model.BankAccount;
import com.spring.userservice.model.User;
import com.spring.userservice.repository.BankAccountRepository;
import com.spring.userservice.requestdto.AddbankAccountRequestDto;
import com.spring.userservice.service.BankService;
import com.spring.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

	private final BankAccountRepository bankAccountRepository;
	private final UserService userService;

	@Override
	public String openBankAccount(AddbankAccountRequestDto addbankAccountRequestDto, String email) {
		Optional<User> user = userService.getUser(email);

		user.ifPresent(u -> {
			if (!u.isKycEnabled()) {
				throw new NotFoundException("kyc", "User has pending KYC!");
			}

			if (u.getBankAccounts().stream()
					.anyMatch(e -> e.getBankName().equals(addbankAccountRequestDto.getBankName()))) {
				throw new AlreadyExistException("BankName", "Account already exists");
			}

			BankAccount bankAccount = new BankAccount();
			bankAccount.setAccountNumber(generateRandom11DigitNumber());
			bankAccount.setBankName(addbankAccountRequestDto.getBankName());
			bankAccount.setBalance(addbankAccountRequestDto.getBalance());
			bankAccount.setStatus("OPEN");
			bankAccount.setUser(u);

			bankAccountRepository.save(bankAccount);
		});

		return "Bank Account Added";
	}

	private static long generateRandom11DigitNumber() {
		long lowerBound = 10000000000L;
		long upperBound = 99999999999L;

		return ThreadLocalRandom.current().nextLong(lowerBound, upperBound);
	}

}
