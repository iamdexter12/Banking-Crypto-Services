package com.spring.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.userservice.model.City;

public interface CityRepository extends JpaRepository<City, Integer> {

}
