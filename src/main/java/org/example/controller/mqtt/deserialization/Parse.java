package org.example.controller.mqtt.deserialization;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import java.util.Objects;
import org.apache.logging.log4j.Logger;

/**
 * Classe per la deserializzazione dei messaggi MQTT.
 */
public class Parse {
    private static final Gson gson = new Gson();
    private static final Logger logger = LogManager.getLogger(Parse.class);

    /**
     * Metodo che, data un messaggio MQTT e una classe generica utilizzata come modello dei dati,
     * deserializza il messaggio.
     * @param payload messaggio MQTT da deserializzare
     * @param klass classe generica di appoggio che fornisce il modello dei dati per la deserializzazione
     * @return istanza della classe passata come argomento
     * @param <T> tipo generico della classe da utilizzare come modello per la deserializzazione
     */
    public <T> T getParsedMessage(String payload, Class<T> klass) {
        logger.info("Funzione getParsedMessage invocata");
        Objects.requireNonNull(klass, "Class cannot be null");
        if(payload.isEmpty()) {
            throw new IllegalArgumentException("Class cannot be empty");
        }

        try {
            return gson.fromJson(payload, klass);
        } catch(JsonSyntaxException e) {
            System.err.println(e.getMessage());
        }
        return null;

    }
}
