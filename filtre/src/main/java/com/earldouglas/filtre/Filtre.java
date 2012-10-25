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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Filtre implements Filter {

	private Log log = LogFactory.getLog(getClass());
	private AddressManager addressManager = new AddressManager();

	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			if (filterConfig.getInitParameter("whiteList") == null
					&& filterConfig.getInitParameter("blackList") == null
					&& filterConfig.getInitParameter("configLocation") == null) {
				useConfigLocation("classpath:filtre.properties");
			} else {
				addressManager.extendWhiteList(filterConfig
						.getInitParameter("whiteList"));
				addressManager.extendBlackList(filterConfig
						.getInitParameter("blackList"));
				useConfigLocation(filterConfig
						.getInitParameter("configLocation"));
			}
		} catch (AddressFormatException addressFormatException) {
			throw new ServletException(addressFormatException);
		}
	}

	private void useConfigLocation(String configLocation)
			throws AddressFormatException {
		ConfigReader configReader = new ConfigReader(configLocation);
		addressManager.extendWhiteList(configReader.getCompositeWhiteList());
		addressManager.extendBlackList(configReader.getCompositeBlackList());
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		boolean accessGranted = false;
		try {
			accessGranted = addressManager.isAccessPermitted(servletRequest
					.getRemoteAddr());
		} catch (AddressFormatException addressFormatException) {
			throw new ServletException(addressFormatException);
		} finally {
			logResult(servletRequest, accessGranted);
			if (accessGranted) {
				filterChain.doFilter(servletRequest, servletResponse);
			}
		}
	}

	private void logResult(ServletRequest servletRequest, boolean accessGranted) {
		StringBuffer logStringBuffer = new StringBuffer();
		logStringBuffer.append("Access ");

		if (servletRequest instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

			logStringBuffer.append(" to '");
			logStringBuffer.append(httpServletRequest.getRequestURI());
			logStringBuffer.append("' ");
		}

		logStringBuffer.append("from ");
		logStringBuffer.append(servletRequest.getRemoteAddr());
		logStringBuffer.append(" ");

		if (accessGranted) {
			logStringBuffer.append("granted.");
		} else {
			logStringBuffer.append("denied.");
		}

		log.info(logStringBuffer);
	}
}
