package com.spring.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.userservice.model.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

}
