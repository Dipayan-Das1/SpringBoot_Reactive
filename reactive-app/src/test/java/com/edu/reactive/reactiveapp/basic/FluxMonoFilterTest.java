package com.edu.reactive.reactiveapp.basic;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoFilterTest {
	List<String> names = Arrays.asList("tom","dick","harry","ryan","pearce");
	
	@Test
	public void filterTest()
	{
		Flux<String> fluxStr =  Flux.fromIterable(names).filter(name -> name.length() > 3).log();
		StepVerifier.create(fluxStr).expectNextCount(4).verifyComplete();
		
	}
}
