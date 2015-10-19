package com.github.fit.undertow;


import com.github.fit.app.MyApplication;
import com.github.fit.rule.JaxRsIntegrationTestRule;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UndertowContainerTest {

    public static final String WIREMOCK_STRING_RESPONSE = "**** HELLO FROM WIREMOCK ******";

    @Rule
    public JaxRsIntegrationTestRule container = new JaxRsIntegrationTestRule(new MyApplication());

    @Before
    public void setUpMockedAdress() {
        System.setProperty("it.ejb.url", "http://localhost:" + container.getWiremockPort() +"/integration/ejb/message");
        System.setProperty("url.remote.proxy", "http://localhost:" + container.getWiremockPort() +"/proxy");
    }

    @Before
    public void configureStub() {
        stubFor(get(urlEqualTo("/integration/ejb/message"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(WIREMOCK_STRING_RESPONSE)));
    }

    @Test
    public void test_my_resource_with_valid_username() throws Exception {
        Response response = ResteasyClientBuilder.newClient()
                .target("http://localhost:"+ container.getAppPort())
                .request(MediaType.TEXT_PLAIN_TYPE)
                .accept(MediaType.TEXT_PLAIN_TYPE)
                .header("username", "thomas")
                .get(Response.class);

        assertNotNull(response);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void test_my_resource_with_valid_username_to_string() throws Exception {
          String response = ResteasyClientBuilder.newClient()
                .target("http://localhost:"+ container.getAppPort())
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header("username", "thomas")
                .get(String.class);

        assertNotNull(response);
        assertTrue(response.contains(WIREMOCK_STRING_RESPONSE));
        //System.out.println("Response message: " + response);
    }
}