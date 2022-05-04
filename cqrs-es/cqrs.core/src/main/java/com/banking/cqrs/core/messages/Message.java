package com.banking.cqrs.core.messages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

//Un comando es un tipo de mensaje que debe tener un identificador único.
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Message {

    private String id;

}
