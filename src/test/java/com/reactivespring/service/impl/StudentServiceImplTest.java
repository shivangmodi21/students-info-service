package com.reactivespring.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.reactivespring.domain.Passport;
import com.reactivespring.domain.Student;
import com.reactivespring.repository.PassportRepository;
import com.reactivespring.repository.StudentRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("test")
class StudentServiceImplTest {

	@Mock
	private StudentRepository studentRepository;

	@Mock
	private PassportRepository passportRepository;

	@InjectMocks
	private StudentServiceImpl studentService;

	@Test
	void testRetrieveAllStudents() {

		// Given
		List<Student> students = List.of(new Student(1L, "Shivang", LocalDate.parse("2005-06-15"), 1L),
				new Student(2L, "Shikha", LocalDate.parse("2008-07-18"), 2L),
				new Student(3L, "Pransh", LocalDate.parse("2012-07-20"), 3L));
		Passport passport = new Passport(1L, "SHIV12345A");

		// When
		when(studentRepository.findAll()).thenReturn(Flux.fromIterable(students));
		when(passportRepository.findById(isA(Long.class))).thenReturn(Mono.just(passport));
		Flux<Student> fluxStudents = studentService.retrieveAllStudents();

		// Then
		StepVerifier.create(fluxStudents).expectNextCount(3).verifyComplete();
	}

	@Test
	void testRetrieveStudentById() {
		// Given
		Long studentId = 1L;
		Student student = new Student(1L, "Shivang", LocalDate.parse("2005-06-15"), 1L);
		Passport passport = new Passport(1L, "SHIV12345A");

		// When
		when(studentRepository.findById(isA(Long.class))).thenReturn(Mono.just(student));
		when(passportRepository.findById(isA(Long.class))).thenReturn(Mono.just(passport));
		Mono<Student> monoStudent = studentService.retrieveStudentById(studentId);

		// Then
		StepVerifier.create(monoStudent).consumeNextWith(dbStudent -> {
			assertNotNull(dbStudent);
			assertEquals("Shivang", dbStudent.getName());
		}).verifyComplete();
	}

	@Test
	void testAddStudent() {
		// Given
		Student student = new Student(1L, "Shivang", LocalDate.parse("2005-06-15"), 1L);

		// When
		when(studentRepository.save(isA(Student.class))).thenReturn(Mono.just(student));
		Mono<Student> monoStudent = studentService.addStudent(student);

		// Then
		StepVerifier.create(monoStudent).consumeNextWith(dbStudent -> {
			assertNotNull(dbStudent);
			assertEquals("Shivang", dbStudent.getName());
		}).verifyComplete();
	}

	@Test
	void testUpdateStudent() {
		// Given
		Long studentId = 1L;
		Student student = new Student(1L, "Shivang", LocalDate.parse("2005-06-15"), 1L);

		// When
		when(studentRepository.save(isA(Student.class))).thenReturn(Mono.just(student));
		when(studentRepository.findById(isA(Long.class))).thenReturn(Mono.just(student));
		Mono<Student> monoStudent = studentService.updateStudent(studentId, student);

		// Then
		StepVerifier.create(monoStudent).consumeNextWith(dbStudent -> {
			assertNotNull(dbStudent);
			assertEquals("Shivang", dbStudent.getName());
		}).verifyComplete();
	}

	@Test
	void testDeleteStudentById() {
		// Given
		Long studentId = 1L;
		Mono<Student> studentMono = Mono.just(new Student(1L, "Shivang", LocalDate.parse("2005-06-15"), 1L));

		// When
		when(studentRepository.save(isA(Student.class))).thenReturn(studentMono);
		when(studentRepository.deleteById(studentId)).thenReturn(Mono.empty());
		Mono<Void> data = studentService.deleteStudentById(studentId);

		// Then
		StepVerifier.create(data).expectNextCount(0).verifyComplete();
	}

}
