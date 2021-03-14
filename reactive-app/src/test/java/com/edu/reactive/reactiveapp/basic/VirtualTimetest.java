package com.edu.reactive.reactiveapp.basic;
import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class VirtualTimetest {
	
	@Test
	public void testWithoutvirtualTime()
	{
		Flux<Long> longFlux = Flux.interval(Duration.ofMillis(10)).take(100).log();
		StepVerifier.create(longFlux).expectSubscription().expectNextCount(100).verifyComplete();
	}

}
