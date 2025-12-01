package org.example.controller.messageDispatcher.DTO;

/**
 * Classe che definisce il modello dei dati usato per serializzare i messaggi MQTT diretti
 * al servizio relativo al server.
 */
public class ServerMessage {
    private long deviceId;
    private String plateNummber;
    private String timestamp;
    private String request;

    /**
     * Costruttore
     * @param deviceId identificatore del dispositivo
     * @param plateNummber numero di targa
     * @param request messaggio testuale
     */
    public ServerMessage(long deviceId, String plateNummber, String request, String timestamp) {
        if(plateNummber.isEmpty() || request.isEmpty() || timestamp.isEmpty()) {
            throw new IllegalArgumentException("NumberPlate and request arguments cannot be empty");
        }
        if(String.valueOf(deviceId).length() == 10) {
            this.deviceId = deviceId;
            this.plateNummber = plateNummber;
            this.timestamp = timestamp;
            this.request = request;
        }
    }
}