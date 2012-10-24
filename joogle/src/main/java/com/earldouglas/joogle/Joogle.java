package com.earldouglas.joogle;

import com.earldouglas.joogle.Library.Func;

public class Joogle {

  public static void main(String args[]) throws Exception {

    if (args.length != 3) {
      System.out.println("Usage: Joogle <jar file> <input> <output>");
      System.exit(0);
    }

    String jarFilename = args[0];
    Library library = new Library(jarFilename);

    String input = args[1];
    String output = args[2];

    for (Func func : library.search(input, output)) {
      System.out.println(func.toString());
    }
  }
}
