package com.edu.reactive.reactiveapp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import com.edu.reactive.reactiveapp.document.ItemCapped;

import reactor.core.publisher.Flux;

public interface ItemCappedRepository extends ReactiveMongoRepository<ItemCapped, String> {
	
	@Tailable
	Flux<ItemCapped> findItemsBy();
}
