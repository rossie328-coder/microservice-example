package org.example.service;

import org.example.controller.messageDispatcher.MessageDispatcher;

/**
 * Classe utilizzata per gestire la logica di business del microservizio.
 */
public class EntryTicketService {
    private final MessageDispatcher dispatcher;
    private long deviceId;
    private String plateNumber;

    /**
     * Costruttore
     * @param mqttBroker url del broker MQTT
     */
    public EntryTicketService(String mqttBroker) {

        this.dispatcher = new MessageDispatcher(mqttBroker);
    }

    /**
     * Metodo per gestire la ricezione del numero di matricola dal server
     * @param deviceId identificatore del dispositivo
     * @param serialNumber numero di matricola
     */
    public void serialNumberReceived(long deviceId, long serialNumber) {
        // controllo i valori ricevuti
        if(String.valueOf(deviceId).length() == 6 && String.valueOf(serialNumber).length() == 10) {
            // invoco metodo del dispatcher per inoltrare il numero di matricola alla macchinetta
            dispatcher.sendTicket(deviceId, serialNumber);
            // invoco metodo del dispatcher per richiedere il numero di targa alla telecamera
            dispatcher.contactCamera(deviceId);
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
            // invio un messaggio alla sbarra per consentire il passaggio dell'auto
            dispatcher.contactBar(deviceId, "open");
            this.plateNumber = plateNumber;
            // invio il numero di targa al server
            //dispatcher.sendTextualMessage(deviceId, plateNumber);
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
        // invio un messaggio alla sbarra per consentire il passaggio dell'auto
        if(deviceId == this.deviceId) {
            // invio il numero di targa al server
            dispatcher.contactServer(deviceId, plateNumber);
        }
    }

}

