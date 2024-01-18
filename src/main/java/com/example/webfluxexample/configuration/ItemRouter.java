package com.example.webfluxexample.configuration;

import com.example.webfluxexample.handler.ItemHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Slf4j
@Configuration
public class ItemRouter {

    @Bean
    public RouterFunction<ServerResponse> itemResponse(ItemHandler itemHandler) {

        return RouterFunctions.route()
                .GET("/api/v1/functional/item", itemHandler::getAllItem)
                .GET("/api/v1/functional/item/{id}", itemHandler::getById)
                .POST("/api/v1/functional/item", itemHandler::create)
                .GET("/api/v1/functional/error", itemHandler::errorRequest)
                .build();

    }
}
