package org.example.controller.mqtt.deserialization.DTO;

/**
 * Classe che definisce il modello deio dati utilizzato per deserializzare i messaggi
 * MQTT provenienti dal server.
 */
public class ServerMessage {
    private long deviceId;
    private long serialNumber;

    public long getDeviceId() {
        return deviceId;
    }

    public long getSerial() {
        return serialNumber;
    }
}
