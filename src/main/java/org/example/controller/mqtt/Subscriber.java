package org.example.controller.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.util.Date;
import java.util.Objects;

/**
 * Classe per creare un subscriber.
 */
public class Subscriber {

    public static final String BROKER_URL = "tcp://localhost:8883";
    private final Date date = new Date();
    private String url;
    public String topic;
    private SubscribeCallback sub;
    String clientId = Long.toString(date.getTime()) + "-sub";
    private MqttClient mqttClient;

    /**
     * Costruttore
     * @param hostUrl url del broker MQTT
     * @param topic topic sul broker
     * @param sub istanza della classe SubscribeCallback
     */
    public Subscriber(String hostUrl, String topic, SubscribeCallback sub) {
        if (!Objects.equals(hostUrl, "")) this.url = hostUrl;
        else this.url = BROKER_URL;
        System.out.println("connecting to: " + this.url);

        if (topic != null && !topic.isEmpty() && sub != null) {
            this.topic = topic;
            this.sub = sub;
        }

        try {
            mqttClient = new MqttClient(url, clientId);


        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Metodo per avviare il subscriber
     */
    public void start() {
        try {
            mqttClient.setCallback(sub);
            mqttClient.connect();

            mqttClient.subscribe(topic);
            System.out.println("Subscriber is now listening to " + topic);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}