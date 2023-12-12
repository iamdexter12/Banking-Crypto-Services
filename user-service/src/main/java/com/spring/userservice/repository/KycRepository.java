package com.spring.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.userservice.model.Kyc;

public interface KycRepository extends JpaRepository<Kyc, Integer>{

}
