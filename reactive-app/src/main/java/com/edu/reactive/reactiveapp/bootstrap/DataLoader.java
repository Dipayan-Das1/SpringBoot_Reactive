package com.edu.reactive.reactiveapp.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.edu.reactive.reactiveapp.document.Item;
import com.edu.reactive.reactiveapp.repository.ItemRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DataLoader implements CommandLineRunner{

	@Autowired
	private ItemRepository itemRepository;
	
	@Override
	public void run(String... args) throws Exception {
		Item item = new Item(null,"Apple","Fruit",5d);
		Item item2 = new Item(null,"Mango","Tropical Fruit",6d);
		Item item3 = new Item(null,"Orange","Winter Fruit",4d);
		Flux.just(item,item2,item3).flatMap(itm -> {
			return itemRepository.findByName(itm.getName()).defaultIfEmpty(itm);
		}).flatMap(itm -> {
			if(itm.getId() == null)
			{
				return itemRepository.save(itm);
			}
			return Mono.just(itm);
		}).blockLast();
		
	}

}
