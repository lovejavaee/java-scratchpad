package com.earldouglas.joogle;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class Library {
  private final Map<String, List<Func>> methodsByInput = new HashMap<String, List<Func>>();
  private final Map<String, List<Func>> methodsByOutput = new HashMap<String, List<Func>>();
  private final Map<String, List<Func>> methodsByName = new HashMap<String, List<Func>>();

  public Library(String jarFilename) throws ClassNotFoundException, SecurityException, IOException {
    InputStream stream = ClassLoader.getSystemResourceAsStream(jarFilename);
    JarInputStream jarFile = new JarInputStream(stream);
    URLClassLoader cl = new URLClassLoader(new URL[] { ClassLoader.getSystemResource(jarFilename) });
    JarEntry jarEntry;
    while ((jarEntry = jarFile.getNextJarEntry()) != null) {
      if (jarEntry != null && jarEntry.getName().endsWith(".class")) {
        String name = jarEntry.getName().replaceAll("/", "\\.").replaceAll("\\.class", "");
        Class<?> clazz = cl.loadClass(name);
        for (Method method : clazz.getMethods()) {
          addMethod(clazz, method);
        }
      }
    }
  }

  private void addMethod(Class<?> type, Method method) {
    String paramsString = paramsString(method);
    Func func = new Func(method, type);
    if (!methodsByInput.containsKey(paramsString)) {
      methodsByInput.put(paramsString, new LinkedList<Func>());
    }
    methodsByInput.get(paramsString).add(func);

    String outputString = method.getReturnType().getSimpleName();
    if (!methodsByOutput.containsKey(outputString)) {
      methodsByOutput.put(outputString, new LinkedList<Func>());
    }
    methodsByOutput.get(outputString).add(func);

    String nameString = method.getName();
    if (!methodsByName.containsKey(nameString)) {
      methodsByName.put(nameString, new LinkedList<Func>());
    }
    methodsByName.get(nameString).add(func);
  }

  private static String paramsString(Method method) {
    StringBuffer sb = new StringBuffer();
    if (method.getParameterTypes().length == 0) {
      sb.append("()");
    } else {
      for (Class<?> paramType : method.getParameterTypes()) {
        sb.append(paramType.getSimpleName());
        sb.append(", ");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  public static class Func {

    public final Method method;
    public final Class<?> type;

    public Func(Method _method, Class<?> _type) {
      method = _method;
      type = _type;
    }

    @Override public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append(type.getName());
      sb.append("#");
      sb.append(method.getName());
      sb.append(" :: ");
      sb.append(paramsString(method));
      sb.append(" -> ");
      sb.append(method.getReturnType().getSimpleName());
      return sb.toString();
    }

    @Override public int hashCode() {
      return method.hashCode() + 37 * type.hashCode();
    }

    @Override public boolean equals(Object obj) {
      return obj != null && toString().equals(obj.toString());
    }
  }

  public List<Func> search(String input, String output) {
    List<Func> candidates = methodsByInput.get(input);
    List<Func> winners = new LinkedList<Func>();

    for (Func func : methodsByOutput.get(output)) {
      if (candidates.contains(func)) {
        winners.add(func);
        System.out.println(func.toString());
      }
    }

    return winners;
  }
}
