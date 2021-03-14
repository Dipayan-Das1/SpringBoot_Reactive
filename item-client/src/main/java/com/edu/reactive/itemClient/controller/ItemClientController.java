package com.edu.reactive.itemClient.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.edu.reactive.itemClient.domain.Item;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class ItemClientController {

	
	private WebClient webClient = WebClient.create("http://localhost:8080");
	
	@GetMapping(path = "/client/retrieve/items", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Item> getAllItems() {
		log.info("Get all items");
		Flux<Item> items = webClient.get().uri("/api/items").accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Item.class);
		return items;
	}
	
	@GetMapping(path = "/client/retrieve/items/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Item> getItemByID(@PathVariable String id) {
		log.info("Retrieve item by id");
		Mono<Item> item = webClient.get().uri("/api/items/{id}",id).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Item.class);
		return item;
	}
	
	@GetMapping(path = "/client/exchange/items", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Item> exchangeItems() {
		log.info("Get all items");
		Mono<ClientResponse> resp = webClient.get().uri("/api/items").accept(MediaType.APPLICATION_JSON).exchange();
		return resp.flatMapMany(response -> {
			return response.bodyToFlux(Item.class);
		});
	}
	
	@GetMapping(path = "/client/exchange/items/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Item> exchangeItemById(@PathVariable String id) {
		log.info("Exchange item by id");
		Mono<ClientResponse> resp = webClient.get().uri("/api/items/{id}",id).accept(MediaType.APPLICATION_JSON).exchange();
		return resp.flatMap(response -> {
			return response.bodyToMono(Item.class);
		});
	}
	
	@PostMapping(path = "/client/exchange/items", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Item>> createItem(@RequestBody Item item) {
		log.info("Create Item");
		Mono<ClientResponse> resp = webClient.post().uri("/api/items").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).bodyValue(item)
				.exchange();
		
		return resp.map(response -> {
			return new ResponseEntity(response.bodyToMono(Item.class), response.statusCode());
			
		});
				 	
	}
	
	@PutMapping(path = "/client/exchange/items/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Item>> updateItem(@RequestBody Item item,@PathVariable String id) {
		log.info("Create Item");
		Mono<ClientResponse> resp = webClient.put().uri("/api/items/{id}",id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).bodyValue(item)
				.exchange();
		
		return resp.map(response -> {
			return new ResponseEntity(response.bodyToMono(Item.class), response.statusCode());
			
		});
				 	
	}
	
	@DeleteMapping(path = "/client/exchange/items/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity> deleteItemById(@PathVariable String id) {
		log.info("Delete item by id");
		Mono<ClientResponse> resp = webClient.delete().uri("/api/items/{id}",id).accept(MediaType.APPLICATION_JSON).exchange();
		return resp.map(response -> {
			return new ResponseEntity(response.statusCode());
		});
	}

}
