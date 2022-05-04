package com.banking.cqrs.core.events;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "eventStore") //documento almacenado en una base de datos mongodb
public class EventModel {

    @Id
    private String id;
    private Date timeStamp;
    private String aggregateIdentifier; //permite obtener lista de eventos
    private String aggregateType;
    private int version;
    private String eventType;
    private BaseEvent event; //captura la informacion del evento


}
