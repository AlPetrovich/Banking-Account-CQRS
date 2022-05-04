package com.banking.cqrs.core.events;
import com.banking.cqrs.core.messages.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

//Message hereda a los comandos y eventos
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEvent extends Message {

    //cada vez que ejecutamos un evento necesitamos recrear el estado del aggregate
    //control de concurrencia
    private int version;
}
