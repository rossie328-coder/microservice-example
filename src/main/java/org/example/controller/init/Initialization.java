package org.example.controller.init;

import org.example.controller.mqtt.SubscribeCallback;
import org.example.controller.mqtt.Subscriber;
import org.example.service.EntryTicketService;
import org.example.controller.mqtt.SubscriberCreation;

/**
 * Classe per inizializzare il microservizio. Accetta le variabili d'ambiente dal dockerfile
 * e inietta le dipendenze.
 */
public class Initialization {

    /**
     * Legge le variabili d'ambiente dal dockerfile e inietta le dipendenze.
     */
    public void start(){

        // leggo le variabili d'ambiente dal dockerfile
        String mqttBroker = System.getenv("MQTT_BROKER");
        String topic = System.getenv("TOPIC");
        String topics = System.getenv("TOPICS");

        // passo le variabili e inietto le dipendenze
        EntryTicketService slot = new EntryTicketService(mqttBroker);
        SubscriberCreation subCreation = new SubscriberCreation(mqttBroker, topics);
        SubscribeCallback subCallback = new SubscribeCallback(slot, subCreation, topics);

        // effettuo le iscrizioni ai topic
        Subscriber sub = new Subscriber(mqttBroker, topic, subCallback);
        sub.start();

    }
}
