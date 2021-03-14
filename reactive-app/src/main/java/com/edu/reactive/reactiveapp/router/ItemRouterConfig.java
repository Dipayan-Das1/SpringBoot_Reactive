package com.edu.reactive.reactiveapp.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.edu.reactive.reactiveapp.handler.ItemHandler;

@Configuration
public class ItemRouterConfig {
	
	
	@Bean
	public RouterFunction<ServerResponse> routeItems(ItemHandler handlerFunction)
	{
		return RouterFunctions.route().GET("/reactive/items", handlerFunction::getAllItems).build()
				.and(RouterFunctions.route().GET("/reactive/items/{id}", handlerFunction::getItemById).build())
				.and(RouterFunctions.route().DELETE("/reactive/items/{id}", handlerFunction::deleteItemById).build())
				.and(RouterFunctions.route().POST("/reactive/items", handlerFunction::createItem).build())
				.and(RouterFunctions.route().PUT("/reactive/items/{id}", handlerFunction::updateItem).build());
	}

}
