package com.example.webfluxexample.handler;

import com.example.webfluxexample.model.ItemModel;
import com.example.webfluxexample.model.SubItemModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class ItemHandler {

    public Mono<ServerResponse> getAllItem(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Flux.just(
                        new ItemModel(UUID.randomUUID().toString(), "Name_1", 10, Collections.emptyList()),
                        new ItemModel(UUID.randomUUID().toString(), "Name_2", 20, List.of(
                                new SubItemModel("Name_0", BigDecimal.valueOf(10000))
                        )),
                        new ItemModel(UUID.randomUUID().toString(),
                                "Name_3",
                                30,
                                Collections.emptyList())),
                        ItemModel.class);
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new ItemModel(serverRequest.pathVariable("id"),
                        "Name_1",
                        10,
                        Collections.emptyList())),
                            ItemModel.class);
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(ItemModel.class)
                .flatMap(item -> {
                    log.info("Item for create {}", item);
                    return  Mono.just(item);
                })
                .flatMap(item -> ServerResponse.created(URI.create("/api/v1/functional/item/" + item.getId())).build());

    }

    public Mono<ServerResponse> errorRequest(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.error(new RuntimeException("Exception in errorRequest!")), String.class)
                .onErrorResume(e -> {
                    log.error("Error for errorRequest", e);

                    return ServerResponse.badRequest().body(Mono.error(e), String.class);
                });
    }
}
