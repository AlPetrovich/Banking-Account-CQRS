package com.banking.cqrs.core.producers;

import com.banking.cqrs.core.events.BaseEvent;

public interface EventProducer {

    //dentro de que canal devemos enviar el mensaje, se indica topic y la data enviada
    void produce(String topic, BaseEvent event);
}
