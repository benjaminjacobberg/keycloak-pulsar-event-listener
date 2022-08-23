package com.github.benjaminjacobberg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.Producer;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

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
            producer.send(objectMapper.writeValueAsString(event).getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        try {
            producer.send(objectMapper.writeValueAsString(adminEvent).getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {

    }
}
