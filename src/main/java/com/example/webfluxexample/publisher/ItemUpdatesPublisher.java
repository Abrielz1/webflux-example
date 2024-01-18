package com.example.webfluxexample.publisher;

import com.example.webfluxexample.model.ItemModel;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class ItemUpdatesPublisher {

    private final Sinks.Many<ItemModel> itemModelUpdatesSink;

    public ItemUpdatesPublisher() {
        this.itemModelUpdatesSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(ItemModel model) {
        itemModelUpdatesSink.tryEmitNext(model);
    }

    public Sinks.Many<ItemModel> getUpdateSink() {
        return itemModelUpdatesSink;
    }
}
