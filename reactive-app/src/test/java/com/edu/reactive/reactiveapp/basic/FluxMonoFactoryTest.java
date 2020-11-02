package com.edu.reactive.reactiveapp.basic;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxMonoFactoryTest {
	
	
	List<String> names = Arrays.asList("tom","dick","harry","ryan","pearce");
	
	@Test
	public void testFluxUsingIterable()
	{
		Flux<String> strFlux = Flux.fromIterable(names).log();
		StepVerifier.create(strFlux).expectNextCount(5).verifyComplete();
	}
	
	@Test
	public void testFluxUsingStream()
	{
		Flux<String> strFlux = Flux.fromStream(names.stream()).log();
		StepVerifier.create(strFlux).expectNextCount(5).verifyComplete();
	}
	
	@Test
	public void testFluxUsingArray()
	{
		Flux<String> strFlux = Flux.fromArray(names.toArray(new String[5])).log();
		StepVerifier.create(strFlux).expectNextCount(5).verifyComplete();
	}
	
	@Test
	public void testEmptyMono()
	{
		Mono<Object> strFlux = Mono.empty().log();
		StepVerifier.create(strFlux).verifyComplete();
	}
	
	@Test
	public void testEmptyMono2()
	{
		Mono<Object> strFlux = Mono.justOrEmpty(null).log();
		StepVerifier.create(strFlux).verifyComplete();
	}
	
	@Test
	public void testFluxRange()
	{
		Flux<Integer> intFlux = Flux.range(10, 5).log();
		StepVerifier.create(intFlux).expectNextCount(5).verifyComplete();
	}

}
