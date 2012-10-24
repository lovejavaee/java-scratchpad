# Testing Web Applications with Jetty

_31 Jan 2010_

An [embedded Jetty server](https://github.com/JamesEarlDouglas/embedded-jetty) provides a quick and easy means of testing web applications. In this example I expand on [A Secure RESTful Web Service](https://github.com/JamesEarlDouglas/secure-rest), which requires manual steps of building and deploying a web application to an existing and configured application server. I introduce an embedded Jetty server which is started as part of testing, and enables tests to run without the manual steps of building and deploying the web application. It also eliminates the need to have a discrete application server available for testing.

The first step is to add an embedded Jetty server to a test case, configured to start prior to the tests and stop once they are complete.

_EmployeeControllerTest.java:_

```java
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
```

The only changes to `EmployeeControllerTest` are to add two methods which run before and after the test case execution, setting up and tearing down the Jetty server, as well as a reference to the Server object itself.

_pom.xml:_

```xml
<dependency>
  <groupId>org.eclipse.jetty</groupId>
  <artifactId>jetty-server</artifactId>
  <version>7.0.1.v20091125</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.eclipse.jetty</groupId>
  <artifactId>jetty-webapp</artifactId>
  <version>7.0.1.v20091125</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>commons-codec</groupId>
  <artifactId>commons-codec</artifactId>
  <version>1.4</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.apache.tomcat</groupId>
  <artifactId>jasper</artifactId>
  <version>6.0.20</version>
  <scope>test</scope>
</dependency>
```

The Maven configuration is changed to add Jetty and a couple of dependencies.

That's all there is to it. Now when `EmployeeControllerTest` is run, it will first set up the Jetty server and deploy the web application from its location within the source tree, run the tests, and take down the Jetty server. This comes at the cost of only a few seconds, and makes testing much easier to automate. 
