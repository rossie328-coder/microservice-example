package org.example.controller.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.example.controller.mqtt.deserialization.DTO.DeviceMessage;
import org.example.service.EntryTicketService;
import java.util.concurrent.*;
import org.example.controller.mqtt.deserialization.Parse;
import java.util.*;
import java.util.function.Consumer;
import java.util.LinkedList;
import java.util.AbstractMap.SimpleEntry;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

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
    private LinkedList<AbstractMap.SimpleEntry<String, MqttMessage>> queue;
    private static final Logger logger = LogManager.getLogger(SubscribeCallback.class);
    private final Set<String> topicsReceived = ConcurrentHashMap.newKeySet();

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
        this.queue = new LinkedList<>();
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
        topicsReceived.add(topic);
        logger.info("Messaggio {} arrivato", message);
        // gestico la ricezione dei messaggi sui topic attraverso una mappa
        if(Objects.isNull(message)) {
            throw new NullPointerException("Message reference cannot be null");
        }

        // Coda per la gestione dei messaggi in arrivo
        queue.add(new SimpleEntry<>(topic, message));

        while(!queue.isEmpty()) {
            // Estraggo il messaggio
            MqttMessage msg = queue.getFirst().getValue();
            // Estraggo il nome del topic ed elimino l'elemento della lista
            String top = queue.remove().getKey();
            logger.info("Topic della richiesta: {}", top);
            if(top.equals("system/entrance/manual/intern/button")) {
                logger.info("Messaggio sul topic {}", "system/entrance/manual/intern/button");
                executor.submit(() -> {
                    logger.info("Avvio un nuovo thread ");

                    String devicePayload = new String(msg.getPayload());

                    logger.info(devicePayload);

                    DeviceMessage messageParsed = parse.getParsedMessage(devicePayload, DeviceMessage.class);
                    logger.info("Messaggio {}: ", messageParsed);
                    logger.info("Messaggio ricevuto sul topic {} dal dispositivo con id: {}", "system/entrance/manual/intern/button", messageParsed.getDeviceId());
                    // richiedo il numero di targa alla telecamera
                    entryTicketService.contactCamera(messageParsed.getDeviceId());

                    // estraggo l'id del dispositivo, lo converto in una stringa
                    // effettuo la sottoscrizione ai topic parametrizzati con il client id del publisher
                    String clientId = Long.toString(messageParsed.getDeviceId());

                    logger.info("Client id estratto: {}", clientId);
                    logger.info("Contesto: {}", this);

                    sub.createSubscriptions(clientId, this);

                     // associo ai topic i metodi per la gestione dei messaggi in arrivo
                    handler.put(String.format(topics.split(",")[0], clientId), manMessage::cameraResponse);
                    handler.put(String.format(topics.split(",")[1], clientId), manMessage::barResponse);
                    handler.put(String.format(topics.split(",")[2], clientId), manMessage::serverResponse);

                    Subscriber sub = new Subscriber("ssl://localhost:8883", "system/entrance/manual/intern/button", this);
                    sub.start();
                });
            }
            /* a ciascun topic (chiave della mappa) è associato un metodo (valore della mappa)
        per la gestione del messaggio
         * */
            handler.get(top).accept(msg);
        }

    }

    /**
     * Metodo che restituisce tutti i topic su cui sono stati ricevuti messaggi
     * @return
     */
    public Set<String> getTopicsReceived() {
        return Collections.unmodifiableSet(topicsReceived);
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

