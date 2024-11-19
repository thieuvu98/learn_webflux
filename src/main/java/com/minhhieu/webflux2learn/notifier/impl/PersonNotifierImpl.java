package com.minhhieu.webflux2learn.notifier.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhhieu.webflux2learn.model.msg.SyncPersonMessage;
import com.minhhieu.webflux2learn.notifier.PersonNotifier;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

@Log4j2
@Component
public class PersonNotifierImpl implements PersonNotifier {
    private final String syncTopic;
    private final ObjectMapper objectMapper;
    private final KafkaSender<Long, byte[]> kafkaSender;

    public PersonNotifierImpl(@Value("${kafka.topic.sync-person}") String syncTopic, ObjectMapper objectMapper,
                              KafkaSender<Long, byte[]> kafkaSender) {
        this.syncTopic = syncTopic;
        this.kafkaSender = kafkaSender;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows(JsonProcessingException.class)
    public Flux<SenderResult<Object>> sync(SyncPersonMessage message) {
        var record = new ProducerRecord<>(syncTopic, message.getId(), objectMapper.writeValueAsBytes(message));
        return kafkaSender.send(Mono.just(SenderRecord.create(record, null)))
                .doOnError(e -> log.error("Can't sync person with id {}, message {}", message.getId(), message));
    }

}
