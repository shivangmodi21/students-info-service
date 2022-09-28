package com.reactivespring.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.reactivespring.domain.Course;

import reactor.core.publisher.Flux;

public interface CourseRepository extends ReactiveCrudRepository<Course, Long> {
	@Query("SELECT * FROM course c JOIN student_course sc on c.course_id = sc.course_id and sc.student_id = $1")
	public Flux<Course> findCoursesByStudentId(Long studentId);
}
