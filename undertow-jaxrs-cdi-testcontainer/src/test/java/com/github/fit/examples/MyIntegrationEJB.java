package com.github.fit.examples;

import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

@Stateless
@Startup
public class MyIntegrationEJB {

    Client myClient = ClientBuilder.newClient();

    public MyIntegrationEJB() {
        System.err.println("Startup Stateless");
    }

    public String foo(){
        String urlEjbIntegration = System.getProperty("it.ejb.url");

        try {
            return myClient.target(urlEjbIntegration).request(MediaType.TEXT_PLAIN_TYPE)
                    .accept(MediaType.TEXT_PLAIN_TYPE)
                    .get(String.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
