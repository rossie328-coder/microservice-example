package org.example.controller.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.example.controller.mqtt.deserialization.DTO.BarCameraMessage;
import org.example.controller.mqtt.deserialization.DTO.ServerMessage;
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
        String barPayload = new String(message.getPayload());
        BarCameraMessage messageParsed = parse.getParsedMessage(barPayload, BarCameraMessage.class);

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
        String barCameraPayload = new String(message.getPayload());
        BarCameraMessage messageParsed = parse.getParsedMessage(barCameraPayload, BarCameraMessage.class);

        // invocazione metodi classe service
        service.manageBarResponse(messageParsed.getDeviceId(), messageParsed.getText());

    }

    public void serverResponse(MqttMessage message) {
        if(Objects.isNull(message)) {
            throw new NullPointerException("Message reference cannot be null");
        }
        // deserializzazione messaggio
        String serverPayload = new String(message.getPayload());
        ServerMessage messageParsed = parse.getParsedMessage(serverPayload, ServerMessage.class);

        // invocazione metodi classe service
        service.serialNumberReceived(messageParsed.getDeviceId(), messageParsed.getSerial());

    }
}
