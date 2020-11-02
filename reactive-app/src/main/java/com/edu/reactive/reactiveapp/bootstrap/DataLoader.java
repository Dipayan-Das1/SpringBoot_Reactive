package com.edu.reactive.reactiveapp.bootstrap;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.edu.reactive.reactiveapp.document.Item;
import com.edu.reactive.reactiveapp.document.ItemCapped;
import com.edu.reactive.reactiveapp.repository.ItemCappedRepository;
import com.edu.reactive.reactiveapp.repository.ItemRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner{

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ItemCappedRepository itemCappedRepository;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	
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
		
		createCappedCollection();	
		dataSetupForCapped();
	}
	
	private void createCappedCollection()
	{
		//fixed size collection
		mongoOperations.dropCollection(ItemCapped.class);
		mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().capped().size(2000).maxDocuments(15));	
	}
	
	private void dataSetupForCapped()
	{
		Flux<ItemCapped> fluxItems = Flux.interval(Duration.ofSeconds(10)).map(val -> {
			return new ItemCapped(null, "Next value "+val, val * 11);
		});
		itemCappedRepository.saveAll(fluxItems).subscribe(itm -> {
			log.info(itm.getId());
		});
	}

}
