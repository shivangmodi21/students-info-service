package com.reactivespring.service;

import com.reactivespring.domain.Student;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {

	Flux<Student> retrieveAllStudents();

	Mono<Student> retrieveStudentById(Long studentId);

	Mono<Student> addStudent(Student student);

	Mono<Student> updateStudent(Long studentId, Student student);

	Mono<Void> deleteStudentById(Long studentId);
	
	Mono<Student> retrieveStudentWithCourses(Long studentId);
}
