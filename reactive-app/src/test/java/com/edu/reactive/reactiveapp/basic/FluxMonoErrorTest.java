package com.edu.reactive.reactiveapp.basic;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoErrorTest {
	@Test
	public void fluxTestExceptionDefaultValue() {
		Flux<String> strFlux = Flux.just("Tom", "Dick", "Harry").
				concatWith(Flux.error(new RuntimeException("Test")))
				.onErrorResume(ex -> {
					ex.printStackTrace();
					return Flux.just("default","default1");
				})
				.log();

		StepVerifier.create(strFlux)
		.expectNext("Tom", "Dick", "Harry")
		.expectNext("default","default1")
		.verifyComplete();
	}
	
	@Test
	public void fluxTestExceptionReturnValue() {
		Flux<String> strFlux = Flux.just("Tom", "Dick", "Harry").
				concatWith(Flux.error(new RuntimeException("Test")))
				.onErrorReturn("fallback value")
				.log();

		StepVerifier.create(strFlux)
		.expectNext("Tom", "Dick", "Harry")
		.expectNext("fallback value")
		.verifyComplete();
	}
	
	@Test
	public void fluxTestExceptionOnErrorMap() {
		Flux<String> strFlux = Flux.just("Tom", "Dick", "Harry").
				concatWith(Flux.error(new RuntimeException("Test")))
				.onErrorMap(e -> {
					return new IllegalArgumentException("Convert exception to another",e);
				})
				.log();

		StepVerifier.create(strFlux)
		.expectNext("Tom", "Dick", "Harry")
		.expectError(IllegalArgumentException.class)
		.verify();
	}
	
	@Test
	public void fluxTestExceptionOnErrorRetry() {
		Flux<String> strFlux = Flux.just("Tom", "Dick", "Harry").
				concatWith(Flux.error(new RuntimeException("Test")))
				.retry(2)
				.log();

		StepVerifier.create(strFlux)
		.expectNext("Tom", "Dick", "Harry")
		.expectNext("Tom", "Dick", "Harry")
		.expectNext("Tom", "Dick", "Harry")
		.expectError(RuntimeException.class)
		.verify();
	}
	
	@Test
	public void fluxTestExceptionOnErrorBackofThenRetry() {
		Flux<String> strFlux = Flux.just("Tom", "Dick", "Harry").
				concatWith(Flux.error(new RuntimeException("Test")))
				.retryBackoff(2, Duration.ofMillis(500))
				.log();

		StepVerifier.create(strFlux)
		.expectNext("Tom", "Dick", "Harry")
		.expectNext("Tom", "Dick", "Harry")
		.expectNext("Tom", "Dick", "Harry")
		.expectError(RuntimeException.class)
		.verify();
	}
	
	
}
