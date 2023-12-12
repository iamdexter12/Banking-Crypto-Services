package com.spring.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.userservice.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>{

}
