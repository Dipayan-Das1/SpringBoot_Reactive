package com.edu.reactive.reactiveapp.basic;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoTimeTest {

	@Test
	public void infiniteSeq() throws InterruptedException
	{
		Flux<Long> infinite = Flux.interval(Duration.ofMillis(200)).log();
		infinite.subscribe(System.out::println);
		Thread.sleep(5000);
	}
	
	@Test
	public void finiteSeqTest() throws InterruptedException
	{
		Flux<Long> finite = Flux.interval(Duration.ofMillis(200)).take(5).log();
		StepVerifier.create(finite).expectNextCount(5).verifyComplete();
	}
	
	@Test
	public void finiteSeqMapTest() throws InterruptedException
	{
		Flux<Long> finite = Flux.interval(Duration.ofMillis(100)).filter(item -> item % 2 == 0).map(item -> item * 10).take(5).log();
		StepVerifier.create(finite).expectNextCount(5).verifyComplete();
	}
}
