package org.example.controller.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.example.service.EntryTicketService;
import java.util.concurrent.*;
import org.example.controller.mqtt.deserialization.DTO.ServerMessage;
import org.example.controller.mqtt.deserialization.Parse;
import java.util.*;
import java.util.function.Consumer;

/**
 * Classe che ospita i metodi per definire le azioni da intraprendere quando cade la connessione,
 * quando viene ricevuto un nuovo messaggio MQTT e quando vengono completate le operazioni.
 */
public class SubscribeCallback implements MqttCallback {
    private final EntryTicketService entryTicketService;
    private final Parse parse;
    private final SubscriberCreation sub;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final String topics;
    private final HashMap<String, Consumer<MqttMessage>> handler;
    private final ManageMessage manMessage;

    /**
     * Costruttore
     * @param entryTicketService istanza della classe di servizio
     * @param sub istanza del subscriber
     * @param topics elenco dei topic proveniente dal dockerfile
     */
    public SubscribeCallback(EntryTicketService entryTicketService, SubscriberCreation sub, String topics) {
        this.entryTicketService = entryTicketService;
        this.parse = new Parse();
        this.sub = sub;
        this.topics = topics;
        this.handler = new HashMap<>();
        this.manMessage = new ManageMessage(entryTicketService);
    }

    /**
     * Metodo per gestire la caduta della connessione
     * @param cause the reason behind the loss of connection.
     */
    @Override
    public void connectionLost(Throwable cause) {
        //This is called when the connection is lost. We could reconnect here.
    }

    /**
     * Metodo per gestire l'arrivo di un nuovo messaggio
     * @param topic name of the topic on the message was published to
     * @param message the actual message.
     * @throws Exception eccezioni
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // gestico la ricezione dei messaggi sui topic attraverso una mappa
        if(Objects.isNull(message)) {
            throw new NullPointerException("Message reference cannot be null");
        }

        if(topic.equals("system/entrance/manual/server/serialNumber")) {
            executor.submit(() -> {

                // creo un thread per ricevere il numero di matricola dal server
                ServerMessage messageParsed = parse.getParsedMessage(message, ServerMessage.class);

                // estraggo l'id del dispositivo, lo converto in una stringa e lo concateno a "-pub"
                // effettuo la sottoscrizione ai topic parametrizzati con il client id del publisher
                String clientId = Long.toString(messageParsed.getDeviceId()) + "-pub";
                sub.createSubscriptions(clientId, this);
                entryTicketService.serialNumberReceived(messageParsed.getDeviceId(), messageParsed.getSerial());

                // associo ai topic i metodi per la gestione dei messaggi in arrivo
                handler.put(String.format(topics.split(",")[0], "/" + clientId), manMessage::cameraResponse);
                handler.put(String.format(topics.split(",")[1], "/" + clientId), manMessage::barResponse);
            });
        }

        /* a ciascun topic (chiave della mappa) è associato un meotodo (valore della mappa)
        per la gestione del messaggio
         * */
        handler.get(topic).accept(message);
    }

    /**
     * Metodo invocato al completamento delle operazioni
     * @param token the delivery token associated with the message.
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //no-op
    }
}

