package org.example;

import org.example.controller.init.Initialization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe per avviare il microservizio
 */
public class Main {
    public static void main(String[] args) {
        final Logger logger = LogManager.getLogger(Main.class);
        logger.info("Microservizio in esecuzione");

        Initialization init = new Initialization();
        init.start();
    }
}