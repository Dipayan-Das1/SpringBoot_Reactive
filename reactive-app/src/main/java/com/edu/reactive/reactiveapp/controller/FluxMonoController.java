package com.edu.reactive.reactiveapp.controller;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class FluxMonoController {

	@GetMapping(path = "/flux",produces=MediaType.APPLICATION_JSON_VALUE)
	public Flux<Integer> getFlux()
	{
		return Flux.just(1,2,3,4,5)
				.log();
	}
	
	@GetMapping(path = "/mono",produces=MediaType.APPLICATION_JSON_VALUE)
	public Flux<String> getMono()
	{
		return Flux.just("Hello")
				.log();
	}
	
	@GetMapping(path = "/fluxStream",produces=MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Long> getFluxStream()
	{
		return Flux.interval(Duration.ofMillis(500))
				.log();
	}
	
}
