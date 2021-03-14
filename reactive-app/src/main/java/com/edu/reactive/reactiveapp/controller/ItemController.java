package com.edu.reactive.reactiveapp.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.edu.reactive.reactiveapp.document.Item;
import com.edu.reactive.reactiveapp.repository.ItemRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

	@GetMapping(path = "/api/items", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Item> getAllItems() {
		log.info("Get all items");
		return itemRepository.findAll();
	}

	@GetMapping(path = "/api/items/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Item>> getItemById(@PathVariable String id) {
		log.info("Get item for {}", id);
		return itemRepository.findById(id).map(itm -> {
			return ResponseEntity.ok().body(itm);
		}).defaultIfEmpty(new ResponseEntity<Item>(HttpStatus.NOT_FOUND));
	}
	
	
	
	@DeleteMapping(path = "/api/items/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity> deleteItemById(@PathVariable String id) {
		log.info("delete item for {}", id);
		return itemRepository.findById(id).log().flatMap(itm -> {
				 itemRepository.delete(itm);
				 return Mono.just(itm);
		}).map(res -> {
			return new ResponseEntity(HttpStatus.OK);
		}).defaultIfEmpty(new ResponseEntity(HttpStatus.NOT_FOUND));
		
	}
	
	@PutMapping(path = "/api/items/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Item>> updateItemById(@PathVariable String id,@RequestBody Item item ) {
		log.info("update item for {}", id);
		item.setId(id);
		return itemRepository.findById(id).log().flatMap(itm -> {
				BeanUtils.copyProperties(item, itm);
				return itemRepository.save(itm);	
		}).map(res -> {
			return  ResponseEntity.ok(res);
		}).defaultIfEmpty(new ResponseEntity(HttpStatus.NOT_FOUND));
		
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(path = "/api/items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Item> createItem(@RequestBody Item item ) {
		log.info("Create Item");
		return itemRepository.save(item);
	}
}
