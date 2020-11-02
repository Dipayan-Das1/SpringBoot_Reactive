package com.edu.reactive.reactiveapp.basic;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxMonoTest {
	
	@Test
	public void fluxTest()
	{
		Flux<String> strFlux = Flux.just("Tom","Dick","Harry").log();
		strFlux.subscribe(System.out::println, exception -> {
			System.out.println(exception);
		});
	}
	
	@Test
	public void fluxTestSteps()
	{
		Flux<String> strFlux = Flux.just("Tom","Dick","Harry").log();
		StepVerifier.create(strFlux).expectNext("Tom").expectNext("Dick").expectNext("Harry").verifyComplete();
	}
	
	@Test
	public void fluxTestStepsWithError()
	{
		Flux<String> strFlux = Flux.just("Tom","Dick","Harry").concatWith(Flux.error(new RuntimeException("Test"))).log();
		StepVerifier.create(strFlux).expectNext("Tom").expectNext("Dick").expectNext("Harry").expectError().verify();
	}
	
	@Test
	public void fluxTestException()
	{
		Flux<String> strFlux = Flux.just("Tom","Dick","Harry").
				concatWith(Flux.error(new RuntimeException("Test"))).log();
		
		strFlux.subscribe(System.out::println, exception -> {
			System.out.println(exception);
		});	
	}
	
	@Test
	public void fluxTestElmsCount()
	{
		Flux<String> strFlux = Flux.just("Tom","Dick","Harry").log();
		StepVerifier.create(strFlux).expectNextCount(3).verifyComplete();
	}
	
	@Test
	public void monoTestStep()
	{
		Mono<String> strMono = Mono.just("Tom").log();
		StepVerifier.create(strMono).expectNext("Tom").verifyComplete();
	}
	
	@Test
	public void monoTestStepWithError()
	{
		Mono errMono = Mono.error(new RuntimeException("Test")).log();
		StepVerifier.create(errMono).expectError(RuntimeException.class).verify();
	}

}
