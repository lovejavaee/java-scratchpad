public class Main {

  /*
  // Java 7 (sans lambda expressions)

  public static Fn1<Integer,Integer> plus(final int n) {
    return new Fn1<Integer,Integer>() {
      public Integer apply(Integer x) {
        return x + n;
      }
    };
  }

  public static Fn1<Integer,Integer> times(final int n) {
    return new Fn1<Integer,Integer>() {
      public Integer apply(Integer x) {
        return x * n;
      }
    };
  }

  public static void main(String[] args) {

    System.out.println(
      Maybe.<Integer>just(1)
           .map(plus(20))
           .map(plus(2))
    ); // just(42)

    System.out.println(
      Maybe.<Integer>nothing()
           .map(plus(20))
           .map(plus(2))
    ); // nothing

    System.out.println(
      List.cons(1, List.cons(1, List.cons(2, List.cons(3, List.<Integer>nil()))))
          .map(plus(20))
          .map(times(2))
    ); // cons(42, cons(42, cons(44, cons(46, nil))))

    System.out.println(
      List.<Integer>nil()
          .map(plus(20))
          .map(times(2))
    ); // nil

    System.out.println(
      new ListFunctor<Integer>(java.util.Arrays.asList(1,1,2,3))
            .map(plus(20))
            .map(times(2))
    ); // [42, 42, 44, 46]

    System.out.println(
      new ListFunctor<Integer>(new java.util.LinkedList<Integer>())
            .map(plus(20))
            .map(times(2))
    ); // []

  }
  */

  // Java 8 (with lambda expressions)
  public static void main(String[] args) {
    
    System.out.println(
      Maybe.just(1)
           .map((x) -> x + 20)
           .map((x) -> x * 2)
    ); // just(42)

    System.out.println(
      Maybe.<Integer>nothing()
           .map((x) -> x + 20)
           .map((x) -> x * 2)
    ); // nothing

    System.out.println(
      List.cons(1, List.cons(1, List.cons(2, List.cons(3, List.nil()))))
          .map((x) -> x + 20)
          .map((x) -> x * 2)
    ); // cons(42, cons(42, cons(44, cons(46, nil))))

    System.out.println(
      List.<Integer>nil()
          .map((x) -> x + 20)
          .map((x) -> x * 2)
    ); // nil

    System.out.println(
      new ListFunctor<Integer>(java.util.Arrays.asList(1,1,2,3))
            .map((x) -> x + 20)
            .map((x) -> x * 2)
    ); // [42, 42, 44, 46]

    System.out.println(
      new ListFunctor<Integer>(new java.util.LinkedList<Integer>())
            .map((x) -> x + 20)
            .map((x) -> x * 2)
    ); // []

  }

}
