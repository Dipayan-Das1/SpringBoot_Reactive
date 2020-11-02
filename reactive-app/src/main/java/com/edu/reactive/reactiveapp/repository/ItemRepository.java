package com.edu.reactive.reactiveapp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.edu.reactive.reactiveapp.document.Item;

import reactor.core.publisher.Flux;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {
	
	Flux<Item> findByName(String name);

}
