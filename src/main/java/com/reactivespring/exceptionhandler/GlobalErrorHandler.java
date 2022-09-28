package com.reactivespring.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.reactivespring.exception.StudentDataException;
import com.reactivespring.exception.StudentNotFoundException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
			
	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		logger.error("Error message is {}", ex.getMessage(), ex);
		DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
		DataBuffer dataBuffer = dataBufferFactory.wrap(ex.getMessage().getBytes());

		if (ex instanceof StudentDataException) {
			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			return exchange.getResponse().writeWith(Mono.just(dataBuffer));
		}
		
		if (ex instanceof StudentNotFoundException) {
			exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
			return exchange.getResponse().writeWith(Mono.just(dataBuffer));
		}
		
		exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		return exchange.getResponse().writeWith(Mono.just(dataBuffer));
	}

}
