package com.reactivespring.exceptionhandler;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {
	public Logger logger = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(WebExchangeBindException.class)
	public ResponseEntity<String> handleRequestBodyError(WebExchangeBindException ex) {
		logger.error("Exception Caught in handleRequestBody Error : {}", ex.getMessage(), ex);
		String errors = ex.getBindingResult().getAllErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).sorted().collect(Collectors.joining(","));
		logger.error("Error is : {}", errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
}
