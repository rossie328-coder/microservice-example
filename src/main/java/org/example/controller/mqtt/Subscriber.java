package org.example.controller.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.Main;

import java.util.*;

/**
 * Classe per creare un subscriber.
 */
public class Subscriber {

    public static final String BROKER_URL = "ssl://localhost:8883";
    private final Date date = new Date();
    String url;
    public String topic;
    private SubscribeCallback sub;
    String clientId = Long.toString(date.getTime()) + "-sub";
    private MqttClient mqttClient;
    final Logger logger = LogManager.getLogger(Main.class);
    private final Set<String> subscribedTopics = new HashSet<>();

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
            MqttConnectOptions options = new MqttConnectOptions();

            // Socket factory che accetta qualunque certificato digitale
            TLSUtil tlsUtil = new TLSUtil();
            options.setSocketFactory(tlsUtil.trustAllSocketFactory());

            // Imposto username e password
            options.setUserName(Credentials.getMqttUsername());
            options.setPassword(Credentials.getMqttPassword().toCharArray());
            mqttClient.connect(options);
            mqttClient.subscribe(topic);
            subscribedTopics.add(topic);
            System.out.println("Subscriber is now listening to " + topic);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> getSubscribedTopics() {
        return Collections.unmodifiableSet(subscribedTopics);
    }

}