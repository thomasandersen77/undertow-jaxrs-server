package com.github.fit.client;

import javax.servlet.ServletException;

import com.github.fit.examples.JaxrsApplication;
import com.github.fit.undertow.UndertowServer;

public class ServerTest {
    public static void main(String[] args) throws ServletException {
        UndertowServer server = new UndertowServer(8080);
        server.startContainer(new JaxrsApplication());
        server.stopContainer();
    }
}
