package com.edu.reactive.reactiveapp.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RequestHandler {

	public Mono<ServerResponse> flux(ServerRequest request) {
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(Flux.just(1, 2, 3, 4, 5).log(),
				Integer.class);
	}
	
	public Mono<ServerResponse> mono(ServerRequest request) {
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just("Hello world!").log(),
				String.class);
	}

}
