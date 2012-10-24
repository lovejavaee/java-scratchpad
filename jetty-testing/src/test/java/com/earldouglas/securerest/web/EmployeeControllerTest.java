package com.earldouglas.securerest.web;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeeControllerTest {

	private static Server server;

	@BeforeClass
	public static void startWebapp() throws Exception {
		server = new Server();

		Connector connector = new SelectChannelConnector();
		connector.setPort(8080);

		server.addConnector(connector);

		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/secure-rest");

		webAppContext.setWar("src/main/webapp");

		server.setHandler(webAppContext);

		server.start();
	}

	@AfterClass
	public static void stopWebapp() throws Exception {
		server.stop();
	}

	@Test
	public void testHtml() throws Exception {
		String responseBody = get("jmcdoe", "jmcdoe", "text/html");

		// Make sure some HTML came back.
		assertTrue(responseBody.trim().startsWith("<table"));

		// Make sure the right attributes came back.
		assertTrue(responseBody.contains(">Id:<"));
		assertTrue(responseBody.contains(">1<"));
		assertTrue(responseBody.contains(">Name:<"));
		assertTrue(responseBody.contains(">Max Power<"));
		assertTrue(responseBody.contains(">Title:<"));
		assertTrue(responseBody.contains(">The Leader<"));
		assertTrue(responseBody.contains(">Salary:<"));
		assertFalse(responseBody.contains(">640000<"));

		responseBody = get("ntwo", "ntwo", "text/html");

		// Make sure some HTML came back.
		assertTrue(responseBody.trim().startsWith("<table"));

		// Make sure the right attributes came back.
		assertTrue(responseBody.contains(">Id:<"));
		assertTrue(responseBody.contains(">1<"));
		assertTrue(responseBody.contains(">Name:<"));
		assertTrue(responseBody.contains(">Max Power<"));
		assertTrue(responseBody.contains(">Title:<"));
		assertTrue(responseBody.contains(">The Leader<"));
		assertTrue(responseBody.contains(">Salary:<"));
		assertTrue(responseBody.contains(">640000<"));
	}

	@Test
	public void testXml() throws Exception {
		String responseBody = get("jmcdoe", "jmcdoe", "application/xml");

		// Make sure some XML came back.
		assertTrue(responseBody.startsWith("<?xml"));

		// Make sure the right attributes came back.
		assertTrue(responseBody.contains("<id>1</id>"));
		assertTrue(responseBody.contains("<name>Max Power</name>"));
		assertTrue(responseBody.contains("<title>The Leader</title>"));
		assertFalse(responseBody.contains("<salary>640000</salary>"));

		responseBody = get("ntwo", "ntwo", "application/xml");

		// Make sure some XML came back.
		assertTrue(responseBody.startsWith("<?xml"));

		// Make sure the right attributes came back.
		assertTrue(responseBody.contains("<id>1</id>"));
		assertTrue(responseBody.contains("<name>Max Power</name>"));
		assertTrue(responseBody.contains("<title>The Leader</title>"));
		assertTrue(responseBody.contains("<salary>640000</salary>"));
	}

	private String get(String username, String password, String acceptHeader) throws Exception {
		HttpClient httpClient = new HttpClient();

		Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
		httpClient.getState().setCredentials(AuthScope.ANY, defaultcreds);

		HttpMethod httpMethod = new GetMethod("http://localhost:8080/secure-rest/directory/employee/1");
		httpMethod.setRequestHeader("Accept", acceptHeader);
		httpClient.executeMethod(httpMethod);
		String responseBody = new String(httpMethod.getResponseBody());
		httpMethod.releaseConnection();

		return responseBody;
	}
}
