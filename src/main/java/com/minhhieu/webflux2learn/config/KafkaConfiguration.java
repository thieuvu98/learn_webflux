package com.minhhieu.webflux2learn.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class KafkaConfiguration {
    private final String bootstrapServers;
    private final String keySerializerClassConfig;
    private final String valueSerializerClassConfig;

    public KafkaConfiguration(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
                              @Value("${spring.kafka.producer.key-serializer}") String keySerializerClassConfig,
                              @Value("${spring.kafka.producer.value-serializer}") String valueSerializerClassConfig) {
        this.bootstrapServers = bootstrapServers;
        this.keySerializerClassConfig = keySerializerClassConfig;
        this.valueSerializerClassConfig = valueSerializerClassConfig;
    }

    @Bean
    public KafkaSender<Long, byte[]> kafkaSender() {
        Map<String, Object> props = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClassConfig,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClassConfig
        );
        SenderOptions<Long, byte[]> senderOptions = SenderOptions.create(props);
        return KafkaSender.create(senderOptions);
    }

}
