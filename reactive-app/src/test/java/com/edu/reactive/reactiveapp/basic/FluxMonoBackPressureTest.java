package com.edu.reactive.reactiveapp.basic;

import org.junit.jupiter.api.Test;

import reactor.core.CoreSubscriber;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoBackPressureTest {

	@Test
	public void backPressuretest() {
		Flux<Integer> intFlux = Flux.range(1, 10).log();
		StepVerifier.create(intFlux).expectSubscription()
		.thenRequest(1).expectNext(1)
		.thenRequest(4).expectNext(2,3,4,5)
		.thenRequest(5).expectNext(6,7,8,9,10)
		.verifyComplete();
	}
	
	@Test
	public void backPressureHandle() {
		Flux<Integer> intFlux = Flux.range(1, 10).log();
		intFlux.subscribe(elm -> {
			System.out.println(elm);
		},
		ex -> ex.printStackTrace(),
		() -> System.out.println("Completed"),
		(subscription) -> {
			subscription.request(3);
		}
		);
	}
	
	@Test
	public void backPressureHandle_CustomSubscriber() {
		Flux<Integer> intFlux = Flux.range(1, 10).log();
		intFlux.subscribe(new BaseSubscriber<Integer>() {
			@Override
			protected void hookOnNext(Integer value) {
				request(1);
				System.out.println(value);
				if(value == 9)
				{
					cancel();
				}
				
			}
		});
	}
}
