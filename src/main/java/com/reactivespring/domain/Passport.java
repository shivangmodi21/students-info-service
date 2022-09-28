package com.reactivespring.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "passport")
public class Passport {

	@Id
	@Column(value = "passport_id")
	private Long passportId;

	@Column(value = "passport_number")
	private String passportNumber;

	public Passport() {
	}

	public Passport(Long passportId, String passportNumber) {
		super();
		this.passportId = passportId;
		this.passportNumber = passportNumber;
	}

	public Long getPassportId() {
		return passportId;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	@Override
	public String toString() {
		return "Passport [passportId=" + passportId + ", number=" + passportNumber + "]";
	}

}
