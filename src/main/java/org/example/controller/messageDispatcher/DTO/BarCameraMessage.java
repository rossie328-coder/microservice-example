package org.example.controller.messageDispatcher.DTO;

/**
 * Classe che definisce il moddello dei dati usato per serializzare i messaggi MQTT diretti
 * ai servizi relativi alla telecamera e alla sbarra.
 */
public class BarCameraMessage {
    private long deviceId;
    private String text;

    /**
     * Costruttore
     * @param deviceId identificatore del dispositivo
     * @param text messaggio testuale
     */
    public BarCameraMessage(long deviceId, String text) {
        if(text.isEmpty()) {
            throw new IllegalArgumentException("Text argument cannot be empty");
        }
        if(String.valueOf(deviceId).length() == 10) {
            this.deviceId = deviceId;
            this.text = text;
        }
    }
}