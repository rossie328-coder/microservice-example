package org.example.controller.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import java.util.Objects;

/**
 * Classe utilizzata per definire il publisher MQTT.
 */
public class Publisher {

    public static final String BROKER_URL = "tcp://localhost:8883";
    private String topic;
    private String message;
    private MqttClient client;
    private String clientId;

    /**
     * Costruttore
     * @param hostUrl url del broker MQTT
     * @param message messaggio MQTT
     * @param topic topic MQTT
     * @param deviceId identificatore del dispositivo
     */
    public Publisher(String hostUrl, String message, String topic, long deviceId) {
        String hurl;
        if(!Objects.equals(hostUrl, "")) hurl = hostUrl;
        else hurl = BROKER_URL;
        System.out.println("connecting to: "+hurl);

        if(message != null && topic != null && deviceId > 0) {
            this.message = message;
            this.topic = topic;
            this.clientId = Long.toString(deviceId) + "-pub";
        }

        try {

            client = new MqttClient(hurl, clientId);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Meotodo per avviare il publisher
     */
    public void start() {

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            // options.setWill(client.getTopic("intern/bar"), "I'm gone :(".getBytes(), 0, false);

            client.connect(options);

            final MqttTopic topic = client.getTopic(this.topic);
            byte[] messageByte = message.getBytes();

            topic.publish(new MqttMessage(messageByte));

            client.disconnect();

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}

