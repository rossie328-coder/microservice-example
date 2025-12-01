package org.example.controller.messageDispatcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controller.messageDispatcher.DTO.BarCameraMessage;
import org.example.controller.messageDispatcher.DTO.ServerMessage;
import org.example.controller.messageDispatcher.DTO.TicketMachineMessage;
import org.example.controller.mqtt.Publisher;
import com.google.gson.Gson;
import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Classe per creare messaggi da inoltrare al broker MQTT.
 */
public class MessageDispatcher {
    private String mqttBroker;
    private static final Logger logger = LogManager.getLogger(MessageDispatcher.class);

    /**
     * Costruttore
     * @param mqttBroker url del broker MQTT
     */
    public MessageDispatcher(String mqttBroker) {
        if(mqttBroker.isEmpty()) {
            throw new IllegalArgumentException("mqttBroker argument cannot be empty");
        }
        this.mqttBroker = mqttBroker;
    }

    /**
     * Metodo per inoltrare la matricola del biglietto alla macchinetta per l'erogazione del biglietto
     * @param deviceId identificatore del dispositivo
     * @param serial numero di matricola
     */
    public void sendTicket(long deviceId, long serial) {
        if(String.valueOf(deviceId).length() == 6 && String.valueOf(serial).length() == 10) {
            // serializzo il messaggio da inviare sul topic
            Gson gson = new Gson();
            TicketMachineMessage message = new TicketMachineMessage(deviceId, serial);
            String msg = gson.toJson(message);
            String topic = String.format("system/entrance/manual/%s/intern/ticket", deviceId);
            // istanzio il publisher e passo i parametri al costruttore
            Publisher pub = new Publisher(mqttBroker, msg, topic, deviceId);
            pub.start();

            logger.info("Numero matricola: {}", serial);
            logger.info("Numero di matricola inviato al dispositivo fisico sul topic {}", topic);
        }
    }

    /**
     * Metodo per richiedere alla telecamera il numero di targa della macchina
     * @param deviceId identificatore del dispositivo
     */
    public void contactCamera(long deviceId) {
        if(String.valueOf(deviceId).length() == 6) {
            // serializzo il messaggio da inviare sul topic
            Gson gson = new Gson();
            BarCameraMessage message = new BarCameraMessage(deviceId, "SendPlateNumber");
            String msg = gson.toJson(message);
            logger.info(msg);
            // istanzio il publisher e passo i parametri al costruttore
            String topic = String.format("system/entrance/manual/%s/intern/camera/request", deviceId);
            logger.info("Topic: {}", topic);
            Publisher pub = new Publisher(mqttBroker, msg, topic, deviceId);
            pub.start();
            logger.info("Richiesta della targa inviata alla telecamera sul topic {}", topic);
        }
    }


    /**
     * Metodo per inviare un messaggio MQTT alla sbarra e richiedere la sua apertura
     * @param deviceId identificatore del dispositivo
     * @param text messaggio testuale
     */
    public void contactBar(long deviceId, String text) {
        if(text.isEmpty()) {
            throw new IllegalArgumentException("text argument cannot be empty");
        }
        if(String.valueOf(deviceId).length() == 6) {
            // serializzo il messaggio da inviare sul topic
            Gson gson = new Gson();
            BarCameraMessage message = new BarCameraMessage(deviceId, text);
            String msg = gson.toJson(message);
            String topic = String.format("system/entrance/manual/%s/intern/bar/request", deviceId);
            // istanzio il publisher e passo i parametri al costruttore
            Publisher pub = new Publisher(mqttBroker, msg, topic, deviceId);
            pub.start();
            logger.info("Richiesta inviata alla sbarra");
        }
    }

    /**
     * Metodo per inviare deviceId, targa, timestamp e messaggio testuale al server per avvisarlo dell'entrata dell'auto
     * @param deviceId identificatore del dispositivo
     * @param plateNumber numero di targa
     */
    public void contactServer(long deviceId, String plateNumber) {
        if(plateNumber.isEmpty()) {
            throw new IllegalArgumentException("numberPlate cannot be empty");
        }
        if(String.valueOf(deviceId).length() == 6 && String.valueOf(plateNumber).length() == 7) {
            // serializzo il messaggio da inviare sul topic
            Gson gson = new Gson();
            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = formatter.format(now);
            ServerMessage message = new ServerMessage(deviceId, plateNumber, timestamp);
            String msg = gson.toJson(message);
            // istanzio il publisher e passo i parametri al costruttore
            Publisher pub = new Publisher(mqttBroker, msg, "system/entrance/manual/server/data", deviceId);
            pub.start();
        }
    }


}

