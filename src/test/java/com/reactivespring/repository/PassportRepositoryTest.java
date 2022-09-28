package com.reactivespring.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureDataMongo
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class PassportRepositoryTest {

	@Autowired
	PassportRepository passportRepository;

	@BeforeEach
	void setUp() {
		List<Passport> passports = List.of(new Passport(null, "SHIV12345A"), new Passport(null, "SHIK12345B"),
				new Passport(null, "PRAN12345D"));

		passportRepository.saveAll(passports).blockLast();

	}

	@AfterEach
	void tearDown() {
		passportRepository.deleteAll().block();
	}

	@Test
	void testRetrieveAllPassports() {
		// Given

		// When
		Flux<Passport> passports = passportRepository.findAll();

		// Then
		StepVerifier.create(passports)
        .recordWith(ArrayList::new)
        .thenConsumeWhile(passport -> true)
        .expectRecordedMatches(elements -> !elements.isEmpty())
        .verifyComplete();
	}

	@Test
	void testRetrievePassportById() {

		// Given

		// When
		Flux<Passport> passports = passportRepository.findAll().log();
		Passport passportFlux = passports.blockFirst();
		Mono<Passport> passport = passportRepository.findById(passportFlux.getPassportId()).log();

		// Then
		StepVerifier.create(passport).assertNext(dbPassport -> {
			assertEquals("SHIV12345A", dbPassport.getPassportNumber());
		}).verifyComplete();
	}

	@Test
	void testAddPassport() {
		// Given
		Passport passport = new Passport(null, "SHIV12345A");

		// When
		Mono<Passport> savedpassport = passportRepository.save(passport).log();

		// Then
		StepVerifier.create(savedpassport).consumeNextWith(dbPassport -> {
			assertNotNull(dbPassport);
			assertNotNull(dbPassport.getPassportId());
			assertEquals("SHIV12345A", dbPassport.getPassportNumber());
		}).verifyComplete();

	}

	@Test
	void testUpdatePassport() {
		// Given
		Passport passport = new Passport(null, "SHIV12345A");

		// When
		Passport savedpassport = passportRepository.save(passport).block();
		Mono<Passport> retrievePassport = passportRepository.findById(savedpassport.getPassportId()).log();
		Passport data = retrievePassport.block();
		data.setPassportNumber("SHIV12345AB");
		Mono<Passport> updatedPassport = passportRepository.save(data);

		// Then
		StepVerifier.create(updatedPassport).consumeNextWith(dbPassport -> {
			assertNotNull(dbPassport);
			assertNotNull(dbPassport.getPassportNumber());
			assertEquals("SHIV12345AB", dbPassport.getPassportNumber());
		}).verifyComplete();
	}
}
