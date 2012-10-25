/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.earldouglas.filtre;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public abstract class FiltreTestSupport {

	private static Server server;

	protected void filtreWebTest(String compositeWhiteList,
			String compositeBlackList, String configLocation, boolean accessible)
			throws Exception {

		if (server != null && server.isRunning()) {
			server.stop();
		}

		FilterHolder filterHolder = new FilterHolder();
		filterHolder.setFilter(new Filtre());

		if (compositeWhiteList != null) {
			filterHolder.setInitParameter("whiteList", compositeWhiteList);
		}
		if (compositeBlackList != null) {
			filterHolder.setInitParameter("blackList", compositeBlackList);
		}
		if (configLocation != null) {
			filterHolder.setInitParameter("configLocation", configLocation);
		}

		server = new Server(8181);

		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		ServletHolder servletHolder = new ServletHolder(new HttpServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void service(HttpServletRequest httpServletRequest,
					HttpServletResponse httpServletResponse)
					throws ServletException, IOException {
				httpServletResponse.getWriter().write("hello, world");
			}
		});

		if (filterHolder != null) {
			context.addFilter(filterHolder, "/*", 0);
		}

		context.addServlet(servletHolder, "/hello");

		server.start();

		String response = getResponse();

		if (accessible) {
			assertEquals("hello, world", response);
		} else {
			assertEquals("", response);
		}

		server.stop();
	}

	private String getResponse() throws IOException {
		HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(
				"http://localhost:8181/hello").openConnection();

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(httpURLConnection.getInputStream()));
		StringBuffer stringBuffer = new StringBuffer();

		String line;
		while ((line = bufferedReader.readLine()) != null) {
			stringBuffer.append(line);
		}
		bufferedReader.close();

		return stringBuffer.toString();
	}
}