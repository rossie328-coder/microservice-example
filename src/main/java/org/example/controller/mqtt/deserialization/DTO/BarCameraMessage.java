package org.example.controller.mqtt.deserialization.DTO;

/**
 * Classe che definisce il modello dei dati utilizzato per deserializzare i messaggi MQTT
 * provenienti dai servizi relativi alla telecamera e alla sbarra
 */
public class BarCameraMessage {
    private long deviceId;
    private String text;

    public long getDeviceId() {
        return deviceId;
    }

    public String getText() {
        return text;
    }
}

