public class Main {

  // Java 8 (with lambda expressions)
  public static void main(String[] args) {

    System.out.println(
      Monads.eta(42, Maybe.nothing())
    ); // just(42)
    
    System.out.println(
      Maybe.just(1)
           .map((x) -> x + 20)
           .map((x) -> x * 2)
           .flatMap((x) -> {
             if (x != 42) return Maybe.nothing();
             else return Maybe.just(x);
           })
    ); // just(42)


    System.out.println(
      List.cons(1, List.cons(1, List.cons(2, List.cons(3, List.nil()))))
          .map((x) -> x + 20)
          .map((x) -> x * 2)
          .flatMap((x) -> {
            if (x != 42) return List.nil();
            else return List.cons(x, List.nil());
          })
    ); // cons(42, cons(42, nil))

    System.out.println(
      ListMonad.<Integer>apply(java.util.Arrays.asList(1,1,2,3))
          .map((x) -> x + 20)
          .map((x) -> x * 2)
          .flatMap((x) -> {
            if (x != 42) return new ListMonad<Integer>(java.util.Arrays.asList());
            else return new ListMonad<Integer>(java.util.Arrays.asList(x));
          })
    ); // [42, 42]

    System.out.println(
      State.<java.util.List<String>,Integer>apply(
             (log) -> {
               log.add("and one");
               return new Tuple2<Integer,java.util.List<String>>(1, log);
             }
           )
           .map((x) -> x + 20)
           .map((x) -> x * 2)
           .flatMap((x) ->
             State.<java.util.List<String>,Integer>apply(
                    (log) -> {
                      if (x != 42) {
                        log.add("boo");
                        return Tuple2.<Integer,java.util.List<String>>apply(-999, log);
                      } else {
                        log.add("yay");
                        return Tuple2.<Integer,java.util.List<String>>apply(x, log);
                      }
                    }
                  )
           )
           .f.apply(new java.util.LinkedList<String>())
    ); // (42, [and one, yay])

  }

}
