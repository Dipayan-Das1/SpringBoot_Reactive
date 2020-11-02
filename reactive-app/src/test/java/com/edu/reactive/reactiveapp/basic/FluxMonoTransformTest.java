package com.edu.reactive.reactiveapp.basic;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoTransformTest {
	
	List<String> names = Arrays.asList("tom","dick","harry","ryan","pearce");
	
	@Test
	public void transformUsingMap() {
		
		Flux<Integer> sizeFlux = Flux.fromIterable(names).map(str -> {
			return str.length(); 
		}).log();
		
		StepVerifier.create(sizeFlux).expectNextCount(5).verifyComplete();
	}
	
	@Test
	public void transformUsingMapFilter() {
		
		Flux<Integer> sizeFlux = Flux.fromIterable(names).map(str -> {
			return str.length(); 
		}).filter(sz -> sz > 3).log();
		
		StepVerifier.create(sizeFlux).expectNextCount(4).verifyComplete();
	}
	
	@Test
	public void transformUsingFlatMap() {
		
		Flux<Integer> fluxInt = Flux.fromIterable(names).flatMap(str -> {
			System.out.println(str);
			return Flux.fromStream(str.chars().boxed()); 
		}).log();
		
		StepVerifier.create(fluxInt).expectNextCount(22).verifyComplete();
	}
}
