package com.alphadevs.wikunum.services.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface KafkaOrderEventConsumer {
    String CHANNELNAME = "binding-in-order-event";

    @Input(CHANNELNAME)
    MessageChannel input();
}
