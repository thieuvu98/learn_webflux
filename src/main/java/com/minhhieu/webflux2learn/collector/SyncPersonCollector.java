package com.minhhieu.webflux2learn.collector;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Set;

@Component
@Log4j2
@SuppressWarnings("all")
public class SyncPersonCollector {
    private final KafkaReceiver<Long, byte[]> receiver;

    public SyncPersonCollector(@Value("${kafka.group.sync-person}") String groupId,
                               @Value("${kafka.topic.sync-person}") String topic,
                               @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
                               @Value("${spring.kafka.consumer.auto-offset-reset}") String autoOffsetReset,
                               @Value("${spring.kafka.consumer.key-deserializer}") String keyDeserializerClassConfig,
                               @Value("${spring.kafka.consumer.value-deserializer}") String valueDeserializerClassConfig) {
        Map<String, Object> props = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializerClassConfig,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializerClassConfig,
                ConsumerConfig.GROUP_ID_CONFIG, groupId,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset
        );
        ReceiverOptions<Long, byte[]> receiverOptions = ReceiverOptions.<Long, byte[]>create(props).subscription(Set.of(topic))
                // With manual ack, if batch update, let's delete 2 below coding lines
                .commitInterval(Duration.ZERO) // Disable auto-commit
                .commitBatchSize(0); // Disable auto-commit based on the number of messages;
        receiver = KafkaReceiver.create(receiverOptions);
    }

    /**
     * If manual ack, it may increase network overhead
     */
    public Flux<byte[]> onSyncPersonMessage() {
        return receiver.receive()
                .map(record -> {
                    log.info("received key={}, value={} from partition={}, offset={}", record.key(),
                            new String(record.value(), StandardCharsets.UTF_8), record.partition(), record.offset());
                    record.receiverOffset().acknowledge();
                    return record.value();
                });
    }

    /**
     * If batch ack, it may increase latency and the risk of reprocessing messages in the event of failure
     * */
//    public void onSyncPersonMessage() {
//        receiver.receive()
//                .bufferTimeout(500, Duration.ofSeconds(5))
//                .flatMap(this::processAndAckBatch)
//                .subscribe();
//    }

//    private Mono<Void> processAndAckBatch(List<ReceiverRecord<String, String>> batch) {
//        // Process the batch of messages
//        batch.forEach(record -> {
//            // Process each message
//        log.info("received key={}, value={} from partition={}, offset={}", record.key(), new String(record.value(), StandardCharsets.UTF_8), record.partition(), record.offset());
//        });
//
//        // Acknowledge the last record in the batch
//        // Just ack the last record, all of previous record and this record will be committed, it ables to reduce network overhead
//        ReceiverRecord<String, String> lastRecord = batch.getLast();
//        lastRecord.receiverOffset().acknowledge();
//        return Mono.empty();
//    }

}
