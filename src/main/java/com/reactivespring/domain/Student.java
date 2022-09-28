package com.reactivespring.domain;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "student")
public class Student {

	@Id
	@Column(value = "student_id")
	private Long studentId;

	@NotBlank(message = "student.name must be present")
	private String name;

	@Column(value = "birth_date")
	private LocalDate birthDate;

	@Column(value = "passport_id")
	private Long passportId;

	@Transient
	private Passport passport;

	@Transient
	private List<Course> courses;

	public Student() {
	}

	public Student(Long studentId, String name, LocalDate birthDate, Long passportId) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.birthDate = birthDate;
		this.passportId = passportId;
	}

	public Student(Long studentId, String name, LocalDate birthDate, Long passportId, Passport passport) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.birthDate = birthDate;
		this.passportId = passportId;
		this.passport = passport;
	}

	public Student(Long studentId, String name, LocalDate birthDate, Long passportId, List<Course> courses) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.birthDate = birthDate;
		this.passportId = passportId;
		this.courses = courses;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Long getPassportId() {
		return passportId;
	}

	public void setPassportId(Long passportId) {
		this.passportId = passportId;
	}

	public Passport getPassport() {
		return passport;
	}

	public void setPassport(Passport passport) {
		this.passport = passport;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	@Override
	public String toString() {
		return "Student [studentId=" + studentId + ", name=" + name + ", birthDate=" + birthDate + ", passportId="
				+ passportId + "]";
	}

}
