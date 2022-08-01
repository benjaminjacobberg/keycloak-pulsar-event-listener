package com.github.benjaminjacobberg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.Producer;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class PulsarEventListenerProvider implements EventListenerProvider {

    private final Producer<byte[]> producer;
    private final ObjectMapper objectMapper;

    public PulsarEventListenerProvider(Producer<byte[]> producer, ObjectMapper objectMapper) {
        this.producer = producer;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onEvent(Event event) {
        try {
            com.github.benjaminjacobberg.Event<Event> pulsarEvent = new com.github.benjaminjacobberg.Event<>(
                    UUID.randomUUID(),
                    event.getUserId(),
                    "IdentityProvider",
                    1,
                    event,
                    event.getType().toString(),
                    LocalDateTime.now()
            );
            String body = objectMapper.writeValueAsString(pulsarEvent);
            producer.send(body.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        try {
            com.github.benjaminjacobberg.Event<AdminEvent> pulsarEvent = new com.github.benjaminjacobberg.Event<>(
                    UUID.randomUUID(),
                    adminEvent.getRealmId(),
                    "IdentityProvider",
                    1,
                    adminEvent,
                    adminEvent.getOperationType().toString() + "_" + adminEvent.getResourceTypeAsString(),
                    LocalDateTime.now()
            );
            String body = objectMapper.writeValueAsString(pulsarEvent);
            producer.send(body.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {

    }
}
