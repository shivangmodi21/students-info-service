package com.reactivespring.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.reactivespring.domain.Passport;

@Repository
public interface PassportRepository extends ReactiveCrudRepository<Passport, Long> {

}
