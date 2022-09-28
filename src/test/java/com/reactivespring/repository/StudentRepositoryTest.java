package com.reactivespring.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.reactivespring.domain.Passport;
import com.reactivespring.domain.Student;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureDataMongo
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class StudentRepositoryTest {

	@Autowired
	StudentRepository studentRepository;

	@BeforeEach
	void setUp() {
		List<Passport> passports = List.of(new Passport(null, "SHIV12345A"), new Passport(null, "SHIK12345B"),
				new Passport(null, "PRAN12345D"));

		List<Student> students = List.of(
				new Student(null, "Shivang", LocalDate.parse("2005-06-15"), passports.get(0).getPassportId()),
				new Student(null, "Shikha", LocalDate.parse("2008-07-18"), passports.get(1).getPassportId()),
				new Student(null, "Pransh", LocalDate.parse("2012-07-20"), passports.get(2).getPassportId()));

		studentRepository.saveAll(students).blockLast();
	}

	@AfterEach
	void tearDown() {
		studentRepository.deleteAll().block();
	}

	@Test
	void testRetrieveAllStudents() {
		// Given

		// When
		Flux<Student> students = studentRepository.findAll().log();

		// Then
		StepVerifier.create(students).expectNextCount(3).verifyComplete();
	}

	@Test
	void testRetrieveStudentById() {
		// Given

		// When
		Flux<Student> students = studentRepository.findAll().log();
		Student studenttFlux = students.blockFirst();
		Mono<Student> student = studentRepository.findById(studenttFlux.getStudentId()).log();

		// Then
		StepVerifier.create(student).assertNext(dbStudent -> {
			assertEquals("Shivang", dbStudent.getName());
		}).verifyComplete();
	}

	@Test
	void testAddStudent() {
		// Given
		Passport passport = new Passport(null, "SHIV12345A");
		Student student = new Student(null, "Aanvi", LocalDate.parse("2008-07-18"), passport.getPassportId());

		// When
		Mono<Student> savedStudent = studentRepository.save(student).log();

		// Then
		StepVerifier.create(savedStudent).consumeNextWith(dbStudent -> {
			assertNotNull(dbStudent);
			assertNotNull(dbStudent.getStudentId());
			assertEquals("Aanvi", dbStudent.getName());
		}).verifyComplete();

	}

	@Test
	void testUpdateStudent() {
		// Given
		Passport passport = new Passport(null, "SHIV12345A");
		Student student = new Student(null, "Aanvi", LocalDate.parse("2008-07-18"), passport.getPassportId());

		// When
		Student savedStudent = studentRepository.save(student).block();
		Mono<Student> retrieveStudent = studentRepository.findById(savedStudent.getStudentId()).log();
		Student data = retrieveStudent.block();
		data.setName("Panna");
		Mono<Student> updatedStudent = studentRepository.save(data);

		// Then
		StepVerifier.create(updatedStudent).consumeNextWith(dbStudent -> {
			assertNotNull(dbStudent);
			assertNotNull(dbStudent.getStudentId());
			assertEquals("Panna", dbStudent.getName());
		}).verifyComplete();
	}
}
