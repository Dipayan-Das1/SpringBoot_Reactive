package com.edu.reactive.reactiveapp.handler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.edu.reactive.reactiveapp.document.Item;
import com.edu.reactive.reactiveapp.repository.ItemRepository;

import reactor.core.publisher.Mono;

@Component
public class ItemHandler {
	
	@Autowired
	private ItemRepository itemRepository;

	public Mono<ServerResponse> getAllItems(ServerRequest req)
	{
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(itemRepository.findAll(),Item.class);
	}
	
	public Mono<ServerResponse> getItemById(ServerRequest req)
	{
		
		String id = req.pathVariable("id");
		System.out.println("Inside get item by id "+id);
		return itemRepository.findById(id).flatMap(itm -> {
			return ServerResponse.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(itm),Item.class);
		}).switchIfEmpty(ServerResponse.notFound().build());
		
	}
	
	public Mono<ServerResponse> deleteItemById(ServerRequest req)
	{
		
		String id = req.pathVariable("id");
		System.out.println("Inside delete item by id "+id);
		Map<String,String> msg = new HashMap<>();
		return itemRepository.findById(id).flatMap(itm -> {
			itemRepository.delete(itm);
			msg.put("message", "Item deleted");
			return ServerResponse.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(msg),Map.class);
		}).switchIfEmpty(ServerResponse.notFound().build());
		
	}
	
	public Mono<ServerResponse> createItem(ServerRequest req)
	{
		Mono<Item> item = req.bodyToMono(Item.class);
		return item.flatMap(itm -> {
			return itemRepository.save(itm);
		} ).flatMap(itm ->{
			Mono<ServerResponse> response = ServerResponse.created(URI.create("/reactive/items/"+itm.getId())).contentType(MediaType.APPLICATION_JSON).bodyValue(itm);
			return response;
		});
	}
	
	public Mono<ServerResponse> updateItem(ServerRequest req)
	{
		String id = req.pathVariable("id");
		Mono<Item> item = req.bodyToMono(Item.class);
		return itemRepository.findById(id).flatMap(itm -> {
			return item.flatMap((Item itemR) -> {
				itemR.setId(id);
				BeanUtils.copyProperties(itemR,item);
				return item;
			});
		}).flatMap(itm -> {
			return itemRepository.save(itm);
		}).flatMap(itm -> {
			Mono<ServerResponse> response = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(itm);
			return response;
		}).switchIfEmpty(ServerResponse.notFound().build());
	}
	
	
}
