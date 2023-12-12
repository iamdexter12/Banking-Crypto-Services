package com.spring.userservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pincodes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pincodeId;
	private String pinCode;

	@OneToOne
	private City city;

}
