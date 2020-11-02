package com.edu.reactive.reactiveapp.repository;

import java.util.List;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.edu.reactive.reactiveapp.document.Item;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class ItemReactiveRepositoryTest {

	@Autowired
	private ItemRepository itemRepository;
	
	
	List<Item> items = Arrays.asList(new Item(null,"Apple","fruit",10d),new Item(null,"Ball","Sport",11d));
	
	
	
	@BeforeEach
	public void setup()
	{		
		itemRepository.deleteAll().thenMany(Flux.fromIterable(items)).flatMap(item -> {
			return itemRepository.save(item);
		}).doOnNext(item -> {
			System.out.println(item.getId());
		}).blockLast();
	}
	
	@Test
	public void getAllItems()
	{
		Flux<Item> items = itemRepository.findAll();
		StepVerifier
		.create(items)
		.expectNextMatches(item -> {return item.getName().equals("Apple");})
		.expectNextMatches(item -> {return item.getName().equals("Ball");})
		.verifyComplete();
	}
	
	@Test
	public void findByName()
	{
		Flux<Item> items = itemRepository.findByName("Apple");
		StepVerifier
		.create(items)
		.expectNextMatches(item -> {return item.getName().equals("Apple");})
		.verifyComplete();
	}
	
	@Test
	public void updateItem()
	{
		Flux<Item> items = itemRepository.findByName("Apple").flatMap(item -> {
			item.setPrice(item.getPrice() + 20);
			return Flux.just(item);
		});
		
		Flux<Item> itemsUpdated = itemRepository.saveAll(items);
		
		StepVerifier.create(itemsUpdated).expectNextMatches(item -> {
			return item.getPrice() == 30d;
		}).verifyComplete();
	}
	
	@Test
	public void deleteItem()
	{
		Flux<Item> items = itemRepository.findByName("Apple").flatMap(item -> {
			return itemRepository.deleteById(Mono.just(item.getId()));
		}).thenMany(itemRepository.findAll());
				
		StepVerifier.create(items).expectNextMatches(item -> {
			return !item.getName().equals("Apple");
		}).verifyComplete();
	}
	
	@Test
	public void getById()
	{
		Mono<Item> itemById = itemRepository.save(new Item(null,"Bat","Sport",12d)).flatMap(item -> {
			String id = item.getId();
			return Mono.just(id);
		}).flatMap(id -> {
			return itemRepository.findById(id);
		});
		
		StepVerifier.create(itemById).expectNextMatches(it -> {
				return 	it.getName().matches("Bat");
		}).verifyComplete();
		
	}
}
