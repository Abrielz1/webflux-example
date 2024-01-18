package com.example.webfluxexample.service;

import com.example.webfluxexample.entity.Item;
import com.example.webfluxexample.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    public Flux<Item> getAll() {

        return repository.findAll();
    }

    public Mono<Item> getById(String id) {

        return repository.findById(id);
    }

    public Mono<Item> getByName(String name) {

        return repository.findByName(name);
    }

    public Mono<Item> save(Item item) {

        item.setId(UUID.randomUUID().toString());
        return repository.save(item);
    }

    public Mono<Item> update(String id, Item item) {

        return getById(id).flatMap(itemForUpdate -> {

            if (StringUtils.hasText(item.getName())) { //not null and not blanc
                itemForUpdate.setName(item.getName());
            }

            if (item.getCount() != null) {
                itemForUpdate.setCount(item.getCount());
            }

            if (item.getSubItems() != null) {
                itemForUpdate.setSubItems(item.getSubItems());
            }

            return repository.save(itemForUpdate);
        });
    }

    public Mono<Void> deleteById(String id) {

       return repository.deleteById(id);
    }
}
