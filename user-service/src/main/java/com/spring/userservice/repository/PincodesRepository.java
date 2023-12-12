package com.spring.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.userservice.model.Pincodes;

public interface PincodesRepository extends JpaRepository<Pincodes, Integer>{

}
