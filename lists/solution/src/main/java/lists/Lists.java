package lists;

public final class Lists {

  public static List<Integer> odds  = odds();  // 1 -> 3 -> 5 -> 7 -> ...
  public static List<Integer> evens = evens(); // 2 -> 4 -> 6 -> 8 -> ...

  public static List<Integer> ints  = combine(odds, evens); // 1 -> 2 -> 3 -> 4 -> ...

  public static <A> List<A> combine(final List<A> xs, final List<A> ys) {
    return new List<A>() {
      public A head() { return xs.head(); }
      public List<A> tail() { return combine(ys, xs.tail()); }
    };
  }

  private static List<Integer> byTwos(final int x) {
    return new List<Integer>() {
      public Integer head() { return x; }
      public List<Integer> tail() { return byTwos(x + 2); }
    };
  }

  public static List<Integer> odds() {
    return byTwos(1);
  }

  public static List<Integer> evens() {
    return byTwos(2);
  }

  public static <B> List<B> take(final int x, final List<B> xs) {
    if (x == 0) {
       return new List<B>() {
         public B head() { throw new java.util.NoSuchElementException("head of empty list"); }
         public List<B> tail() { throw new java.lang.UnsupportedOperationException("tail of empty list"); }
       };
    } else {
      return new List<B>() {
        public B head() { return xs.head(); }
        public List<B> tail() { return take(x - 1, xs.tail()); }
      };
    }
  }

  public static String show(List<?> list) {
    try {
      return list.head().toString() + " : " + show(list.tail());
    } catch (java.util.NoSuchElementException e) {
      return "[]";
    }
  }

}
