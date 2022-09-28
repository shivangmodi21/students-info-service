package com.reactivespring.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reactivespring.domain.Student;
import com.reactivespring.service.StudentService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/students")
@Slf4j
public class StudentController {

	private StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping
	public ResponseEntity<Flux<Student>> retriveAllStudents() {
		Flux<Student> students = studentService.retrieveAllStudents();
		return ResponseEntity.ok().body(students);
	}

	@GetMapping("/{studentId}")
	public ResponseEntity<Mono<Student>> retrieveStudentById(@PathVariable Long studentId) {
		return ResponseEntity.ok().body(studentService.retrieveStudentById(studentId));
	}

	@PostMapping
	public ResponseEntity<Mono<Student>> addStudent(@RequestBody @Valid Student student) {
		return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudent(student));
	}

	@PutMapping("/{studentId}")
	public ResponseEntity<Mono<Student>> updateStudent(@PathVariable Long studentId,
			@RequestBody @Valid Student student) {
		return ResponseEntity.ok().body(studentService.updateStudent(studentId, student));
	}

	@DeleteMapping("/{studentId}")
	public ResponseEntity<Mono<Void>> deleteStudentById(@PathVariable Long studentId) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(studentService.deleteStudentById(studentId));
	}

	@GetMapping("/{studentId}/courses")
	public ResponseEntity<Mono<Student>> retrieveStudentWithCourses(@PathVariable Long studentId) {
		return ResponseEntity.ok().body(studentService.retrieveStudentWithCourses(studentId));
	}
}
