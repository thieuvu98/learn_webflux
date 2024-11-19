package com.minhhieu.webflux2learn.notifier;

import com.minhhieu.webflux2learn.model.msg.SyncPersonMessage;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.SenderResult;

public interface PersonNotifier {
    Flux<SenderResult<Object>> sync(SyncPersonMessage message);
}
