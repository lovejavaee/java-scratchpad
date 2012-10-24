# A Simple Embedded Jetty Server

_18 Jul 2009_

Jetty makes for a very simple servlet container when testing or developing embedded web applications. It is quite capable and often satisfactory over alternatives such as JBoss or Tomcat. My favorite "feature" is that Jetty consists entirely of two JAR files, coming in at just under 700 KB.

The following code will set up a Jetty server with a basic HTTP connector open on port 8080. It prepares a single web application, located under the folder `src/main/webapp`, and exposes it on the context `/mywebapp`.

```java
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
```

This can be tested with curl:

```bash
$ curl http://localhost:8080/mywebapp/
Hello, world!
```

When it is not convenient to load web application content from the file system, it can be loaded from the class path by replacing the `setWar()` call with:

```java
webappcontext.setWar(ServerRunner.class.getClassLoader().getResource("com/earldouglas/embeddedjetty/mywebapp").toExternalForm())
```

This will load the web application from the `com.earldouglas.embeddedjetty.mywebapp` package, under which resides the usual `WEB-INF/`, `web.xml`, etc. 