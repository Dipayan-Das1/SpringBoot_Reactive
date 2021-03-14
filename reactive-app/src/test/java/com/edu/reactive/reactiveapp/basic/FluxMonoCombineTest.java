package com.edu.reactive.reactiveapp.basic;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

public class FluxMonoCombineTest {
	
	@Test
	public void combineFlux() {
		Flux<String> flux1 = Flux.just("A","B","C");
		Flux<String> flux2 = Flux.just("D","E","F");
		Flux<String> merged = Flux.merge(flux1,flux2).log();
		StepVerifier.create(merged).expectNextCount(6).verifyComplete();	
	}
	
	@Test
	public void combineFlux_concat() {
		Flux<String> flux1 = Flux.just("A","B","C");
		Flux<String> flux2 = Flux.just("D","E","F");
		Flux<String> merged = Flux.concat(flux1,flux2).log();
		StepVerifier.create(merged).expectNextCount(6).verifyComplete();	
	}
	
	@Test
	public void combineFlux_zip() {
		Flux<String> flux1 = Flux.just("A","B","C");
		Flux<String> flux2 = Flux.just("D","E","F");
		Flux<String> merged = Flux.zip(flux1,flux2,(e1,e2) -> {
			return e1+","+e2;
		}).log();
		StepVerifier.create(merged).expectNext("A,D","B,E","C,F").verifyComplete();	
	}
	
	@Test
	public void combineFlux_WithDelay() {
		Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
		Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
		Flux<String> merged = Flux.merge(flux1,flux2).log();
		StepVerifier.create(merged).expectNextCount(6).verifyComplete();	
	}
	
	//consumes flux 2 only after consuming all elements of flux 1
	@Test
	public void combineFluxConcat_WithDelay() {
		Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
		Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
		Flux<String> merged = Flux.concat(flux1,flux2).log();
		StepVerifier.create(merged).expectNextCount(6).verifyComplete();	
	}
}
