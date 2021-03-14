package com.edu.reactive.itemClient.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Item {
	
	private String id;
	private String name;
	private String description;
	private Double price;

}

