package org.example.controller.init;

import org.example.controller.mqtt.SubscribeCallback;
import org.example.controller.mqtt.Subscriber;
import org.example.service.EntryTicketService;
import org.example.controller.mqtt.SubscriberCreation;

import java.util.Objects;
import java.util.Set;

/**
 * Classe per inizializzare il microservizio. Accetta le variabili d'ambiente dal dockerfile
 * e inietta le dipendenze.
 */
public class Initialization {

    Subscriber sub;
    SubscribeCallback subCallback;

    /**
     * Passa l'URL del broker, i nomi dei topic e inietta le dipendenze.
     */
    public void start(){

        String mqttBroker = "ssl://localhost:8883";
        String topic = "system/entrance/manual/intern/button";
        String topics = "system/entrance/manual/%s/intern/camera/response" +
                "," +
                "system/entrance/manual/%s/intern/bar/response" + "," +
                "system/entrance/manual/%s/server/data";

        // passo le variabili e inietto le dipendenze
        EntryTicketService slot = new EntryTicketService(mqttBroker);
        SubscriberCreation subCreation = new SubscriberCreation(mqttBroker, topics);
        this.subCallback = new SubscribeCallback(slot, subCreation, topics);

        // effettuo l'iscrizione al topic
        this.sub = new Subscriber(mqttBroker, topic, subCallback);
        sub.start();

    }

    public Set<String> getSubscribedTopic() {
        return sub.getSubscribedTopics();
    }

    public Set<String> getAllSubscribedTopics() {
        return subCallback.getTopicsReceived();
    }

}
