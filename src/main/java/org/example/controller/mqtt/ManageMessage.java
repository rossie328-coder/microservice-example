package org.example.controller.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.example.controller.mqtt.deserialization.DTO.BarCameraMessage;
import org.example.controller.mqtt.deserialization.Parse;
import org.example.service.EntryTicketService;
import java.util.Objects;

/**
 * Classe utilizzata per definire i metodi associati ai topic ai quali è iscritto il servizio.
 */
public class ManageMessage {
    private final Parse parse;
    private final EntryTicketService service;

    /**
     * Costruttore
     * @param service istanza della classe di servizio
     */
    public ManageMessage(EntryTicketService service) {
        if(Objects.isNull(service)) {
            throw new NullPointerException("Class pointer cannot be null");
        }
        this.parse = new Parse();
        this.service = service;
    }

    /**
     *
     * Metodo per gestire la ricezione di un messaggio MQTT da parte del servizio relativo alla telecamera
     * @param message messaggio MQTT ricevuto
     */
    // metodo per la gestione del messaggio sul topic "camera/response"
    public void cameraResponse(MqttMessage message) {
        if(Objects.isNull(message)) {
            throw new NullPointerException("Message reference cannot be null");
        }
        // deserializzazione messaaggio
        BarCameraMessage messageParsed = parse.getParsedMessage(message, BarCameraMessage.class);

        // invocazione metodi classe service
        service.manageCameraResponse(messageParsed.getDeviceId(), messageParsed.getText());

    }

    /**
     * Metodo per gestire la ricezione di un messaggio MQTT da parte del servizio relativo alla sbarra
     * @param message messaggio MQTT ricevuto
     */
    // metodo per la gestione del messaggio sul topic "bar/response"
    public void barResponse(MqttMessage message) {
        if(Objects.isNull(message)) {
            throw new NullPointerException("Message reference cannot be null");
        }
        // deserializzazione messaggio
        BarCameraMessage messageParsed = parse.getParsedMessage(message, BarCameraMessage.class);

        // invocazione metodi classe service
        service.manageBarResponse(messageParsed.getDeviceId(), messageParsed.getText());

    }
}
