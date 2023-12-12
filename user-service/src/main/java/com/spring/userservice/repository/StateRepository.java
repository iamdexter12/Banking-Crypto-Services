package com.spring.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.userservice.model.State;

public interface StateRepository extends JpaRepository<State, Integer>{

}
