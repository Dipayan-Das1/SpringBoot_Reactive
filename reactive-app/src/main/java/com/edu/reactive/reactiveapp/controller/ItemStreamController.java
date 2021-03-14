package com.edu.reactive.reactiveapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.reactive.reactiveapp.document.ItemCapped;
import com.edu.reactive.reactiveapp.repository.ItemCappedRepository;

import reactor.core.publisher.Flux;

@RequestMapping("/rest/stream/api")
@RestController
public class ItemStreamController {
	
	@Autowired
	private ItemCappedRepository itemCappedRepository;
	
	@GetMapping(path="/items",produces=MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<ItemCapped> getItems()
	{
		return itemCappedRepository.findItemsBy();
	}

}
