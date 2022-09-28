package com.reactivespring.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reactivespring.domain.Course;
import com.reactivespring.domain.Passport;
import com.reactivespring.domain.Student;
import com.reactivespring.exception.StudentNotFoundException;
import com.reactivespring.repository.CourseRepository;
import com.reactivespring.repository.PassportRepository;
import com.reactivespring.repository.StudentRepository;
import com.reactivespring.service.StudentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentServiceImpl implements StudentService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private StudentRepository studentRepository;
	private PassportRepository passportRepository;
	private CourseRepository courseRepository;

	public StudentServiceImpl(StudentRepository studentRepository, PassportRepository passportRepository,
			CourseRepository courseRepository) {
		this.studentRepository = studentRepository;
		this.passportRepository = passportRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public Flux<Student> retrieveAllStudents() {
		return studentRepository.findAll().flatMap(student -> {
			Mono<Passport> monoPassport = passportRepository.findById(student.getPassportId());
			return monoPassport.map(passport -> new Student(student.getStudentId(), student.getName(),
					student.getBirthDate(), student.getPassportId(), passport));
		});
	}

	@Override
	public Mono<Student> retrieveStudentById(Long studentId) {
		return studentRepository.findById(studentId).flatMap(student -> {
			Mono<Passport> monoPassport = passportRepository.findById(student.getPassportId());
			return monoPassport.map(passport -> new Student(student.getStudentId(), student.getName(),
					student.getBirthDate(), student.getPassportId(), passport));
		}).switchIfEmpty(Mono.error(new StudentNotFoundException("Student not found with the given ID " + studentId)));
	}

	@Override
	public Mono<Student> addStudent(Student student) {
		logger.info("Studnet obj is ", student);
		return studentRepository.save(student);
	}

	@Override
	public Mono<Student> updateStudent(Long studentId, Student student) {
		Mono<Student> existingStudent = studentRepository.findById(studentId).switchIfEmpty(
				Mono.error(new StudentNotFoundException("Student not found with the given ID " + studentId)));
		return existingStudent.flatMap(dbStudent -> {
			dbStudent.setName(student.getName());
			dbStudent.setBirthDate(student.getBirthDate());
			dbStudent.setPassportId(student.getPassportId());
			return studentRepository.save(dbStudent);
		}).switchIfEmpty(Mono.empty());
	}

	@Override
	public Mono<Void> deleteStudentById(Long studentId) {
		return studentRepository.deleteById(studentId);
	}

	@Override
	public Mono<Student> retrieveStudentWithCourses(Long studentId) {
		return studentRepository.findById(studentId).flatMap(student -> {
			Mono<List<Course>> coursesListMono = courseRepository.findCoursesByStudentId(studentId).collectList();
			return coursesListMono.map(courses -> new Student(student.getStudentId(), student.getName(),
					student.getBirthDate(), student.getPassportId(), courses));
		}).switchIfEmpty(Mono.error(new StudentNotFoundException("Student not found with the given ID " + studentId)));
	}

}
