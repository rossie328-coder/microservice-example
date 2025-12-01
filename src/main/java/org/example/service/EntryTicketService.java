package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controller.messageDispatcher.MessageDispatcher;

/**
 * Classe utilizzata per gestire la logica di business del microservizio.
 */
public class EntryTicketService {
    private final MessageDispatcher dispatcher;
    private long deviceId;
    private String plateNumber;
    private static final Logger logger = LogManager.getLogger(EntryTicketService.class);

    /**
     * Costruttore
     * @param mqttBroker url del broker MQTT
     */
    public EntryTicketService(String mqttBroker) {
        this.dispatcher = new MessageDispatcher(mqttBroker);
    }

    // contatto la telecamera per richiedere la targa
    public void contactCamera(long deviceId) {
        if(String.valueOf(deviceId).length() == 6) {
            this.deviceId = deviceId;
            logger.info("Sto contattando la telecamera.");
            // invoco metodo del dispatcher per richiedere il numero di targa alla telecamera
            dispatcher.contactCamera(deviceId);
        } else {
            logger.warn("La lunghezza del deviceId è errata!");
        }
    }

    /**
     * Metodo per gestire la ricezione del numero di matricola dal server
     * @param deviceId identificatore del dispositivo
     * @param serialNumber numero di matricola
     */
    public void serialNumberReceived(long deviceId, long serialNumber) {
        // controllo i valori ricevuti
        if(String.valueOf(deviceId).length() == 6 && String.valueOf(serialNumber).length() == 10) {
            logger.info("Numero di matricola del biglietto ricevuto dal server");
            // invoco metodo del dispatcher per inoltrare il numero di matricola alla macchinetta
            dispatcher.sendTicket(deviceId, serialNumber);

            // invio un messaggio alla sbarra per consentire il passaggio dell'auto
            dispatcher.contactBar(deviceId, "open");
        } else {
            logger.warn("Il numero di matricola o l'identificatore del dispositivo non rispettano di vincoli di lunghezza");
        }

    }

    /**
     * Metodo per gestire il messaggio della telecamera (il numero di targa)
     * @param deviceId identificatore del dispositivo
     * @param plateNumber numero di targa
     */
    public void manageCameraResponse(long deviceId, String plateNumber) {
        // controllo che l'id del dispositivo sia quello giusto
        // invio un messaggio alla sbarra per consentire il passaggio dell'auto
        if(deviceId == this.deviceId && plateNumber.length() == 7) {
            logger.info("Targa di numero {} ricevuta dalla telecamera", plateNumber);

            this.plateNumber = plateNumber;
            // invio il numero di targa al server
            dispatcher.contactServer(deviceId, plateNumber);
        } else {
            logger.warn("Il numero di targa ricevuto dalla telecamera non rispetta i vincoli di lunghezza");
        }
    }

    /**
     * Metodo per gestire la risposta dal servizio relativo alla sbarra
     * @param deviceId identificatore del dispositivo
     * @param text messaggio testuale
     */
    public void manageBarResponse(long deviceId, String text) {
        if(text.isEmpty()) {
            throw new IllegalArgumentException("Text argument cannot be empty");
        }
        // controllo che l'id del dispositivo sia quello giusto
        // e contatto il seerver
        if(deviceId == this.deviceId) {
            logger.info("Messaggio ricevuto dalla sbarra");
            // invio il numero di targa al server
            dispatcher.contactServer(deviceId, plateNumber);
        } else {
            logger.warn("L'identificatore del dispositivo contenuto nel messaggio MQTT è diverso da quello atteso");
        }
    }

}

