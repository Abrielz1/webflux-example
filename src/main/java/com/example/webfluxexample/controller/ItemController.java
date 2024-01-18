package com.example.webfluxexample.controller;

import com.example.webfluxexample.entity.Item;
import com.example.webfluxexample.model.ItemModel;
import com.example.webfluxexample.publisher.ItemUpdatesPublisher;
import com.example.webfluxexample.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private final ItemUpdatesPublisher publisher;

    @GetMapping
    public Flux<ItemModel> getAll() {

        return itemService.getAll().map(ItemModel::from);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ItemModel>> getById(@PathVariable String id) {

        return itemService.getById(id)
                .map(ItemModel::from)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity
                .notFound()
                .build());
    }

    @GetMapping("/by-name")
    public Mono<ResponseEntity<ItemModel>> getByName(@RequestParam String name) {

        return itemService.getByName(name)
                .map(ItemModel::from)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity
                .notFound()
                .build());
    }

    @PostMapping
    public Mono<ResponseEntity<ItemModel>> create(@RequestBody ItemModel item) {

        return itemService.save(Item.from(item))
                .map(ItemModel::from)
                .doOnSuccess(publisher::publish)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ItemModel>> update(@PathVariable String id, @RequestBody ItemModel item) {

        return itemService.update(id, Item.from(item))
                .map(ItemModel::from)
                .map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity
                .notFound()
                .build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteByID(@PathVariable String id) {

        return itemService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ItemModel>> getItemUpdates() {
        return publisher.getUpdateSink()
                .asFlux()
                .map(i -> ServerSentEvent
                .builder(i)
                .build());
    }
}
