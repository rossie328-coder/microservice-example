package org.example.controller.mqtt;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe contenente un metodo per effettuare le sottoacrizioni a una serie di topic
 */
public class SubscriberCreation {
    private final String topics;
    private final String mqttBroker;
    private static final Logger logger = LogManager.getLogger(SubscriberCreation.class);

    /**
     * Costruttore
     * @param mqttBroker url del broker MQTT
     * @param topics stringa contenente una serie di topic separati da una virgola
     */
    public SubscriberCreation(String mqttBroker, String topics) {
        if(topics.isEmpty() || mqttBroker.isEmpty()) {
            throw new IllegalArgumentException("Arguments cannot be empty");
        }
        this.topics = topics;
        this.mqttBroker = mqttBroker;
    }

    /**
     * Metodo che, data una serie di topic, crea le sottoscrizioni
     * @param clientId client id
     * @param subCallback istanza della classe SubscribeCallabck
     */
    public void createSubscriptions(String clientId, SubscribeCallback subCallback) {
        if(clientId.isEmpty()) {
            throw new IllegalArgumentException("Argument cannot be empty");
        }
        logger.info("Iscrizione ai topic: {}", topics);
        for(String topic : topics.split(",")) {
            Subscriber sub = new Subscriber(mqttBroker, String.format(topic, clientId), subCallback);
            sub.start();
        }
        logger.info("Iscrizione ai topic effettuata");
    }
}
