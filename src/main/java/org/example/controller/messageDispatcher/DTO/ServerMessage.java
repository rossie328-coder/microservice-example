package org.example.controller.messageDispatcher.DTO;

/**
 * Classe che definisce il modello dei dati usato per serializzare i messaggi MQTT diretti
 * al servizio relativo al server.
 */
public class ServerMessage {
    private long deviceId;
    private String plateNumber;
    private String timestamp;

    /**
     * Costruttore
     * @param deviceId identificatore del dispositivo
     * @param plateNumber numero di targa
     */
    public ServerMessage(long deviceId, String plateNumber, String timestamp) {
        if(plateNumber.isEmpty() || timestamp.isEmpty()) {
            throw new IllegalArgumentException("NumberPlate and request arguments cannot be empty");
        }
        if(String.valueOf(deviceId).length() == 6) {
            this.deviceId = deviceId;
            this.plateNumber = plateNumber;
            this.timestamp = timestamp;
        }
    }
}