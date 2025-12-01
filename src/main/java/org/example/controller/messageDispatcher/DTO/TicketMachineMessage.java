package org.example.controller.messageDispatcher.DTO;

/**
 * Classe che definisce il modello dei dati usato per serializzare i messaggi MQTT
 * diretti al servizio relativo alla macchinetta per l'erogazione del biglietto.
 */
public class TicketMachineMessage {
    private long deviceId;
    private long serial;

    /**
     * Costruttore
     * @param deviceId identificatore del dispositivo
     * @param serial numero di matricola
     */
    public TicketMachineMessage(long deviceId, long serial) {
        if(String.valueOf(deviceId).length() == 6 && String.valueOf(serial).length() == 10) {
            this.deviceId = deviceId;
            this.serial = serial;
        }
    }
}