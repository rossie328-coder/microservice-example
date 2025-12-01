package org.example.controller.mqtt.deserialization;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.Objects;

/**
 * Classe per la deserializzazione dei messaggi MQTT.
 */
public class Parse {
    private Gson gson;

    /**
     * Costruttore
     */
    public Parse() {
        Gson gson = new Gson();
    }

    /**
     * Metodo che, data un messaggio MQTT e una classe generica utilizzata come modello dei dati,
     * deserializza il messaggio.
     * @param message messaggio MQTT da deserializzare
     * @param klass classe generica di appoggio che fornisce il modello dei dati per la deserializzazione
     * @return istanza della classe passata come argomento
     * @param <T> tipo generico della classe da utilizzare come modello per la deserializzazione
     */
    public <T> T getParsedMessage(MqttMessage message, Class<T> klass) {
        Objects.requireNonNull(klass, "Class cannot be null");
        if(message.toString().isEmpty()) {
            throw new IllegalArgumentException("Class cannot be empty");
        }
        String payload = new String(message.getPayload());
        return gson.fromJson(payload, klass);
    }
}
