package fr.fierdecoder.ajaxcrawlsimulator.web.main;

import net.codestory.http.WebServer;

public class Main {
    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.start();
    }
}
