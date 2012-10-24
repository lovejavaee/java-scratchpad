# Wrangling the Class Path with Download Extensions

_11 Sep 2009_

Setting the class path can be quite bothersome when you have many separate JAR files to deal with. An easy way to manage obese class paths is with download extensions. Through a list of JAR files in the `MANIFEST.MF` file within a given JAR file, you can automatically include an arbitrary number of JAR files on the class path.

Let's imagine we have the following two classes in our project:

```java
package com.earldouglas.greeter;

public class DefaultGreeter {

  public String getGreeting() {
    return "Hello World!";
  }
}
```

```java
package com.earldouglas.greeter;

public class DefaultGreeterRunner {

  public static void main(String[] arguments) {
    System.out.println(new DefaultGreeter().getGreeting());
  }
}
```

Now let's imagine that DefaultGreeter is packaged into `greeter.jar`, while `DefaultGreeterRunner` is packaged into `greeter-runner.jar`.

We can run `DefaultGreeterRunner` from the command line with:

```bash
java -classpath greeter.jar;greeter-runner.jar com.earldouglas.greeter.DefaultGreeterRunner
```

By using download extensions, however, we can trim the command to use a single JAR file in the class path:

```bash
java -classpath greeter-runner.jar com.earldouglas.greeter.DefaultGreeterRunner
```

This is accomplished simply by modifying the `MANIFEST.MF` file within the `META-INF` folder in `greeter-runner.jar`, and adding the dependent JAR files to the `Class-Path` header.
_
_MANIFEST.MF:_

```
Manifest-Version: 1.0
Created-By: 1.5.0_21 (Sun Microsystems Inc.)
Class-Path: greeter.jar
```

Remember that each line in the manifest must be no more than 72 characters long, and to span a long line, you simply prepend each continuation line with two spaces.

See the [Download Extensions Java Tutorial](http://java.sun.com/docs/books/tutorial/ext/basics/download.html) for more detailed information. 
