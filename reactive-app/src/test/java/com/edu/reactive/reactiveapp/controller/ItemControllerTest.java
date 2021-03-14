package com.edu.reactive.reactiveapp.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.edu.reactive.reactiveapp.document.Item;
import com.edu.reactive.reactiveapp.repository.ItemRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@WebFluxTest
public class ItemControllerTest {

	@Autowired
	private WebTestClient webClient;
	
	@MockBean
	private ItemRepository itemRepo;
	
	List<Item> items = Arrays.asList(new Item(UUID.randomUUID().toString(),"Apple","fruit",10d),
			new Item(UUID.randomUUID().toString(),"Ball","Sport",11d));
	
	@Test
	public void getAllItems()
	{
		when(itemRepo.findAll()).thenReturn(Flux.fromIterable(items));
		Flux<Item> items = webClient.get().uri("/api/items").accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().is2xxSuccessful()
		.returnResult(Item.class).getResponseBody();
		
		StepVerifier.create(items).expectNextCount(2).verifyComplete();
	}
	
	@Test
	public void getItembyId()
	{
		Item item = items.get(0); 
		when(itemRepo.findById(item.getId())).thenReturn(Mono.just(item));
		Flux<Item> items = webClient.get().uri("/api/items/"+item.getId()).accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().is2xxSuccessful()
		.returnResult(Item.class).getResponseBody();
		
		StepVerifier.create(items).expectNextMatches(itm -> {
			return itm.getId().equals(item.getId());
		}).verifyComplete();
	}
	
	@Test
	public void getItembyIdNotFound()
	{
		Item item = items.get(0); 
		when(itemRepo.findById(item.getId())).thenReturn(Mono.empty());
		webClient.get().uri("/api/items/"+item.getId()).accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().is4xxClientError();	
	}
	
	@Test
	public void createItem()
	{
		Item create = new Item(null,"Car","vehicle",1000d);
		when(itemRepo.save(any(Item.class))).thenReturn(Mono.just(create));
		Flux<Item> item = webClient.post().uri("/api/items").accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(create), Item.class)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isCreated()
		.returnResult(Item.class)
		.getResponseBody();
		
		StepVerifier.create(item).expectNextMatches(itm -> {
			return	itm.getName().equals("Car");
		}).verifyComplete();
	}
	
	@Test
	public void deleteItembyId()
	{
		Item item = items.get(0); 
		when(itemRepo.findById(item.getId())).thenReturn(Mono.just(item));
		when(itemRepo.delete(any(Item.class))).thenReturn(Mono.empty());
		webClient.delete().uri("/api/items/"+item.getId()).accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().is2xxSuccessful();	
	}
	
	@Test
	public void deleteItembyIdNotFound()
	{
		Item item = items.get(0); 
		when(itemRepo.findById(item.getId())).thenReturn(Mono.empty());
		when(itemRepo.delete(any(Item.class))).thenReturn(Mono.empty());
		webClient.delete().uri("/api/items/"+item.getId()).accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isNotFound();	
	}
	
	@Test
	public void updateItembyId()
	{
		Item item = items.get(0); 
		when(itemRepo.findById(item.getId())).thenReturn(Mono.just(item));
		when(itemRepo.save(any(Item.class))).thenReturn(Mono.just(item));
		webClient.put().uri("/api/items/"+item.getId()).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).bodyValue(item)
		.exchange()
		.expectStatus().is2xxSuccessful();	
	}
	
	@Test
	public void updateItembyId_NotFound()
	{
		Item item = items.get(0); 
		when(itemRepo.findById(item.getId())).thenReturn(Mono.empty());
		webClient.put().uri("/api/items/"+item.getId()).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).bodyValue(item)
		.exchange()
		.expectStatus().isNotFound();	
	}
}
