package com.joey.chain.network;

import java.io.IOException;

public class VerySimpleTest extends LibgdxTest {

    private Server server;

    public void setUp() throws Exception {     
    	super.setUp();
        server = new Server();
        server.startServer();
    }

    public void tearDown() throws IOException {
        server.stopServer();
    }

    public void testOne() {
        String testString = "Hello World";
        String response = Client.sendString(testString);
        System.out.println("Response : "+response);
        assertTrue(response.equals(testString));
    }
}