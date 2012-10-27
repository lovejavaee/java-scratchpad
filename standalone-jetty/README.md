# A Self-Contained Runnable Web Application

_18 Jan 2010_

I thought it would be fun to see if I could create a completely self-contained runnable web application that wasn't bound to the traditional application server plus WAR file pattern. After playing with [embedded Jetty](https://github.com/JamesEarlDouglas/embedded-jetty), and Maven's [jar](http://maven.apache.org/plugins/maven-jar-plugin/), [dependency](http://maven.apache.org/plugins/maven-dependency-plugin/), and [assembly](http://maven.apache.org/plugins/maven-assembly-plugin/) plugins, I came up with a working solution. One option I decided not to go with is a monolithic JAR file tool such as [One-JAR](http://one-jar.sourceforge.net/), because I didn't want to introduce any third party class loading.

## Domain

I put together a very simple Spring MVC "Hello world!" application using Freemarker. Below is the controller, view template, and Spring context configuration, as well as the Jetty server runner.

_HelloController.java:_

```java
@Controller
public class HelloController {

  @RequestMapping("/hello")
  public void hello(Model model) {
    model.addAttribute("greeting", "Hello world!");
  }
}
```

_hello.ftl:_

```xml
<html>
  <head>
    <title>
      hello
    </title>
  <body>
    ${greeting}
  </body>
</html>
```

_mvc-config.xml:_

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
  xmlns:mvc="http://www.springframework.org/schema/mvc"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd
    http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd">

  <context:component-scan base-package="com.earldouglas.selfcontained" />

  <mvc:annotation-driven />

  <bean id="freemarkerConfig"
    class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
    <property name="templateLoaderPath" value="classpath:com/earldouglas/selfcontained/" />
  </bean>

  <bean id="viewResolver"
    class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
    <property name="cache" value="false" />
    <property name="suffix" value=".ftl" />
  </bean>
</beans>
```

_ServerRunner.java:_

```java
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
```

This `ServerRunner` differs from the one I describe in [A Simple Embedded Jetty Server](https://github.com/JamesEarlDouglas/embedded-jetty) in that it doesn't expect a WAR file or expanded web application directory with the conventional `WEB-INF` directory structure. In this `ServerRunner`, the Spring `DispatcherServlet` is added directly to the Jetty context, eliminating the need for a `WEB-INF` directory, `web.xml` file, etc. This is not necessarily a good thing; straying from a standard means you're rolling your own convention and you could have trouble integrating the pattern with other parties. In this case, the goal is to develop a simple embedded web application so there's no need to stick to the standard.

## Packaging

Maven makes packing this code very easy. The first step is creating a JAR file containing the above code.

_pom.xml:_

```xml
<plugin>
  <artifactId>maven-jar-plugin</artifactId>
  <configuration>
    <archive>
      <manifest>
        <mainClass>com.earldouglas.selfcontained.ServerRunner</mainClass>
        <addClasspath>true</addClasspath>
        <classpathPrefix>lib/</classpathPrefix>
      </manifest>
    </archive>
  </configuration>
</plugin>
```

The Maven JAR plugin packages the compiled classes and resources into a JAR file and sets the `Main-Class` and `Class-Path` parameters in the `MANIFEST.MF` file. In this case, the class path entries are all preceded by `lib/`. This will let the dependency JAR files be packaged under a `lib` subdirectory later.

_pom.xml:_

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-dependency-plugin</artifactId>
  <executions>
    <execution>
      <id>copy-dependencies</id>
      <phase>package</phase>
      <goals>
        <goal>copy-dependencies</goal>
      </goals>
      <configuration>
        <outputDirectory>${project.build.directory}/lib</outputDirectory>
        <overWriteReleases>false</overWriteReleases>
        <overWriteSnapshots>false</overWriteSnapshots>
        <overWriteIfNewer>true</overWriteIfNewer>
      </configuration>
    </execution>
  </executions>
</plugin>
```

The Maven Dependency plugin copies the Maven dependencies into the a directory under the build path. This will allow inclusion of dependent JAR files in the final package.

_pom.xml:_

```xml
<plugin>
  <artifactId>maven-assembly-plugin</artifactId>
  <executions>
    <execution>
      <id>assembly</id>
      <phase>package</phase>
      <goals>
        <goal>assembly</goal>
      </goals>
      <configuration>
        <descriptors>
          <descriptor>src/main/assembly/package.xml</descriptor>
        </descriptors>
      </configuration>
    </execution>
  </executions>
</plugin>
```

The Maven Assembly plugin performs the final packaging as specified by the `package.xml` descriptor.

_package.xml:_

```xml
<?xml version="1.0" encoding="UTF-8"?>
<assembly>
  <id>package</id>
  <formats>
    <format>tar.gz</format>
  </formats>
  <fileSets>
    <fileSet>
      <directory>target</directory>
      <outputDirectory></outputDirectory>
      <includes>
        <include>*.jar</include>
      </includes>
    </fileSet>
    <fileSet>
      <directory>target/lib</directory>
      <outputDirectory>lib</outputDirectory>
      <includes>
        <include>*.jar</include>
      </includes>
    </fileSet>
  </fileSets>
</assembly>
```

The `package.xml` descriptor generates a `.tar.gz` file containing the project JAR file as well as the dependency JAR files under a lib subdirectory.

Now a package can be created by running:

```bash
mvn package
```

This package can be extracted anywhere, and the web application run with:

```bash
java -jar selfcontained.jar
```

