package com.edu.reactive.reactiveapp.basic;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

public class HotColdReactiveTest {

	@Test
	public void coldPublisherTest() throws InterruptedException {
		Flux<Integer> intflux = Flux.range(1, 10).delayElements(Duration.ofMillis(10));
		intflux.subscribe(elm -> {
			System.out.println(elm);
		});
		
		
		
		intflux.subscribe(elm -> {
			System.out.println(elm);
		});
		
		///this is a cold publisher, both subscribers get all the elements from flux
		Thread.sleep(2000);

	}
	
	@Test
	public void hotPublisherTest() throws InterruptedException {
		Flux<Integer> intflux = Flux.range(1, 10).delayElements(Duration.ofMillis(20));
		
		ConnectableFlux<Integer> hotflux = intflux.publish();
		hotflux.connect();
		hotflux.subscribe(elm -> {
			System.out.println("Subscriber 1 "+elm);
		});
		
		Thread.sleep(100);
		
		hotflux.subscribe(elm -> {
			System.out.println("Subscriber 2 "+elm);
		});
		
		///this is a hot publisher, both subscribers only the elements left
		Thread.sleep(2000);

	}
}
