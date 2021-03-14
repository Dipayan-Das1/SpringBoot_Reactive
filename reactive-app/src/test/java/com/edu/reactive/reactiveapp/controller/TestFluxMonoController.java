package com.edu.reactive.reactiveapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@WebFluxTest
public class TestFluxMonoController {
	
	@Autowired
	private WebTestClient webClient;
	
	@Test
	public void getFlux()
	{
		Flux<Integer> resFlux = webClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isOk()
		.returnResult(Integer.class)
		.getResponseBody();
		StepVerifier.create(resFlux).expectNext(1,2,3,4,5).verifyComplete();
	}
	
	@Test
	public void getMono()
	{
		EntityExchangeResult<String> result = webClient.get().uri("/mono").accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isOk()
		.expectBody(String.class).returnResult();
		assertEquals("Hello", result.getResponseBody());

	}
	
	@Test
	public void getFluxApproach2()
	{
		webClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Integer.class)
		.hasSize(5);
	}
	
	@Test
	public void getFluxApproach3()
	{
		EntityExchangeResult<List<Integer>> result = webClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Integer.class)
		.returnResult();	
		assertEquals(5, result.getResponseBody().size());
		
	}
	
	@Test
	public void getFluxStream()
	{
		Flux<Long> resFlux = webClient.get().uri("/fluxStream").accept(MediaType.APPLICATION_STREAM_JSON).exchange()
		.expectStatus().isOk()
		.returnResult(Long.class)
		.getResponseBody();
		StepVerifier.create(resFlux).expectNext(0L,1L,2L,3L,4L,5L).thenCancel().verify();
	}
	
}
