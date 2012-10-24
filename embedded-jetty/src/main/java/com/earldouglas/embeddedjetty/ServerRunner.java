package com.earldouglas.embeddedjetty;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class ServerRunner {

    public static void main(String[] arguments) throws Exception {
        Server server = new Server();

        Connector connector = new SelectChannelConnector();
        connector.setPort(8080);

        server.addConnector(connector);

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/mywebapp");

        webAppContext.setWar("src/main/webapp");
        // webAppContext.setWar(ServerRunner.class.getClassLoader().getResource("com/earldouglas/embeddedjetty/mywebapp").toExternalForm())

        server.setHandler(webAppContext);

        server.start();
        server.join();
    }
}