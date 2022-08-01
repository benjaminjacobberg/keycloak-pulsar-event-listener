package com.github.benjaminjacobberg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class PulsarEventListenerProviderFactory implements EventListenerProviderFactory {
    private Producer<byte[]> producer;
    private ObjectMapper objectMapper;

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new PulsarEventListenerProvider(producer, objectMapper);
    }

    @Override
    public void init(Config.Scope scope) {
        String pulsarAddress = System.getenv("PULSAR_ADDRESS");
        String pulsarTopic = System.getenv("PULSAR_TOPIC");
        String pulsarName = System.getenv("PULSAR_PRODUCER_NAME");

        try {
            objectMapper = new ObjectMapper();
            this.producer = PulsarClient.builder()
                    .serviceUrl(pulsarAddress != null ? pulsarAddress : "pulsar://localhost:6650")
                    .build()
                    .newProducer()
                    .producerName(pulsarName != null ? pulsarName : "identity-provider-producer")
                    .topic(pulsarTopic != null ? pulsarTopic : "identity-provider-topic")
                    .create();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "pulsar";
    }
}
