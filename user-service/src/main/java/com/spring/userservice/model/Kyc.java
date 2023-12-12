package com.spring.userservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kyc {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer kycId;
	private String aadharCard;
	private String panCard;
	private String status;
	private String email;

}
