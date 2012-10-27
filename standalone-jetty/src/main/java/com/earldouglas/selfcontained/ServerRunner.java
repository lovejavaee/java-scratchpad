package com.earldouglas.selfcontained;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.springframework.web.servlet.DispatcherServlet;

public class ServerRunner {

	public static void main(String[] arguments) throws Exception {

		Server server = new Server(8080);
		Context context = new Context(server, "/", Context.SESSIONS);

		DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet
				.setContextConfigLocation("classpath:com/earldouglas/selfcontained/mvc-config.xml");

		ServletHolder servletHolder = new ServletHolder(dispatcherServlet);
		context.addServlet(servletHolder, "/*");

		server.start();
		server.join();

	}
}
