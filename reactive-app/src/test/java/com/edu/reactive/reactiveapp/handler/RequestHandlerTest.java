package com.edu.reactive.reactiveapp.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class RequestHandlerTest {
	
	@Autowired
	private WebTestClient webClient;
	
	@Test
	public void getFlux()
	{
		Flux<Integer> resFlux = webClient.get().uri("/functional/flux").accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isOk()
		.returnResult(Integer.class)
		.getResponseBody();
		StepVerifier.create(resFlux).expectNext(1,2,3,4,5).verifyComplete();
	}
	
	@Test
	public void getMono()
	{
		EntityExchangeResult<String> result = webClient.get().uri("/functional/mono").accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isOk()
		.expectBody(String.class).returnResult();
		assertEquals("Hello", result.getResponseBody());

	}

}
