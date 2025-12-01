package org.example;

import org.example.controller.init.Initialization;

/**
 * Classe per avviare il microservizio
 */
public class Main {
    public static void main(String[] args) {
        Initialization init = new Initialization();
        init.start();
    }
}