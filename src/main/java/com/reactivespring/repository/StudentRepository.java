package com.reactivespring.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.reactivespring.domain.Student;


public interface StudentRepository extends ReactiveCrudRepository<Student, Long> {

}
