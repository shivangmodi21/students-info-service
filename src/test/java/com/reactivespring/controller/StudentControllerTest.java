package com.reactivespring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.reactivespring.domain.Student;
import com.reactivespring.exception.StudentNotFoundException;
import com.reactivespring.service.StudentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = StudentController.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class StudentControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private StudentService studentServiceMock;

	static String STUDENTS_INFO_URL = "/v1/students";

	@Test
	void testRetriveAllStudents() {
		List<Student> students = List.of(new Student(1L, "Shivang", LocalDate.parse("2005-06-15"), 1L),
				new Student(2L, "Shikha", LocalDate.parse("2008-07-18"), 2L),
				new Student(3L, "Pransh", LocalDate.parse("2012-07-20"), 3L));

		// When
		when(studentServiceMock.retrieveAllStudents()).thenReturn(Flux.fromIterable(students));

		// Then
		webTestClient.get().uri(STUDENTS_INFO_URL).exchange().expectStatus().is2xxSuccessful()
				.expectBodyList(Student.class).hasSize(3);
	}

	@Test
	void testRetrieveStudentById() {
		// Given
		Long studentId = 3L;
		Student student = new Student(3L, "Pransh", LocalDate.parse("2012-07-20"), 3L);

		// When
		when(studentServiceMock.retrieveStudentById(isA(Long.class))).thenReturn(Mono.just(student));

		// Then
		webTestClient.get().uri(STUDENTS_INFO_URL + "/{studentId}", studentId).exchange().expectStatus()
				.is2xxSuccessful().expectBody(Student.class).consumeWith(studentEntityExchangeResult -> {
					Student dbStudent = studentEntityExchangeResult.getResponseBody();
					assertNotNull(dbStudent);
					assertEquals("Pransh", dbStudent.getName());
				});
	}

	@Test
	void testRetrieveStudentById_404NotFound() {
		// Given
		Long studentId = 5L;

		// When
		when(studentServiceMock.retrieveStudentById(isA(Long.class))).thenReturn(
				Mono.error(new StudentNotFoundException("Student not found with the given ID " + studentId)));

		// Then
		webTestClient.get().uri(STUDENTS_INFO_URL + "/{studentId}", studentId).exchange().expectStatus().isNotFound()
				.expectBody(String.class).consumeWith(errorEntityExchangeResult -> {
					String errorMessage = errorEntityExchangeResult.getResponseBody();
					assertEquals("Student not found with the given ID " + studentId, errorMessage);
				});
	}

	@Test
	void testAddStudent() {
		// Given
		Student student = new Student(null, "Shikha", LocalDate.parse("2012-07-20"), 2L);

		// When
		when(studentServiceMock.addStudent(isA(Student.class)))
				.thenReturn(Mono.just(new Student(2L, "Shikha", LocalDate.parse("2012-07-20"), 2L)));

		// Then
		webTestClient.post().uri(STUDENTS_INFO_URL).bodyValue(student).exchange().expectStatus().isCreated()
				.expectBody(Student.class).consumeWith(studentEntityExchangeResult -> {
					Student dbStudent = studentEntityExchangeResult.getResponseBody();
					assertNotNull(dbStudent);
					assertNotNull(dbStudent.getStudentId());
					assertEquals("Shikha", dbStudent.getName());
				});
	}

	@Test
	void testAddStudent_validation() {
		// Given
		Student student = new Student(null, "", LocalDate.parse("2012-07-20"), 2L);

		// When
		when(studentServiceMock.addStudent(isA(Student.class)))
				.thenReturn(Mono.just(new Student(2L, "", LocalDate.parse("2012-07-20"), 2L)));

		// Then
		webTestClient.post().uri(STUDENTS_INFO_URL).bodyValue(student).exchange().expectStatus().isBadRequest()
				.expectBody(String.class).consumeWith(stringEntityExchangeResult -> {
					String errors = stringEntityExchangeResult.getResponseBody();
					String expectedErrorMessage = "student.name must be present";
					assertEquals(expectedErrorMessage, errors);
				});
	}

	@Test
	void testAddStudent_500InternalServerError() {
		// Given
		Student student = new Student(null, "Shikha", LocalDate.parse("2012-07-20"), null);

		// When
		when(studentServiceMock.addStudent(isA(Student.class))).thenThrow(new NullPointerException());

		// Then
		webTestClient.post().uri(STUDENTS_INFO_URL).bodyValue(student).exchange().expectStatus().is5xxServerError();
	}

	@Test
	void testUpdateStudent() {
		// Given
		Long studentId = 1L;
		Student student = new Student(null, "Panna", LocalDate.parse("2012-07-20"), 1L);

		// When
		when(studentServiceMock.updateStudent(isA(Long.class), isA(Student.class)))
				.thenReturn(Mono.just(new Student(1L, "Panna", LocalDate.parse("2012-07-20"), 1L)));

		// Then
		webTestClient.put().uri(STUDENTS_INFO_URL + "/{studentId}", studentId).bodyValue(student).exchange()
				.expectStatus().is2xxSuccessful().expectBody(Student.class).consumeWith(studentEntityExchangeResult -> {
					Student dbStudent = studentEntityExchangeResult.getResponseBody();
					assertNotNull(dbStudent);
					assertNotNull(dbStudent.getStudentId());
					assertEquals("Panna", dbStudent.getName());
				});
	}

	@Test
	void testDeleteStudentById() {
		// Given
		Long studentId = 1L;

		// When
		when(studentServiceMock.deleteStudentById(isA(Long.class))).thenReturn(Mono.empty());

		// Then
		webTestClient.delete().uri(STUDENTS_INFO_URL + "/{studentId}", studentId).exchange().expectStatus()
				.isNoContent();
	}

}
