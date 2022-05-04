package com.banking.account.cmd.infrastructure;
import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.account.cmd.domain.EventStoreRepository;
import com.banking.cqrs.core.events.BaseEvent;
import com.banking.cqrs.core.events.EventModel;
import com.banking.cqrs.core.exceptions.AggregateNotFoundException;
import com.banking.cqrs.core.exceptions.ConcurrencyException;
import com.banking.cqrs.core.infrastructure.EventStore;
import com.banking.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class AccountEventStore implements EventStore {

    @Autowired
    private EventStoreRepository eventStoreRepository;

    //Toma la data y me envia a mi kafka
    @Autowired
    private EventProducer eventProducer;

    //persisto mis eventos en la base de datos
    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        //buscar eventos existentes
        var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }
        var version = expectedVersion;
        for (var event : events) {
            event.setVersion(++version);
            var eventModel = EventModel.builder()
                    .timeStamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .event(event)
                    .build();
            //almacenar evento en base de dato MongoDB
            var persistedEvent = eventStoreRepository.save(eventModel);
            //si el evento se almaceno de manera correcta en mi base de datos MONGO podemos producir este evento para kafka
            if ( !persistedEvent.getId().isEmpty() ) {
                //producir un evento para kafka, primer parametro topic donde se aloja la data, segundo la data
                eventProducer.produce(event.getClass().getSimpleName(), event);
            }
        }

    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (eventStream == null || eventStream.isEmpty()) {
            throw new AggregateNotFoundException("No events found for aggregate with id " + aggregateId);
        }

        return eventStream.stream().map(eventModel -> eventModel.getEvent()).collect(java.util.stream.Collectors.toList());
    }
}
