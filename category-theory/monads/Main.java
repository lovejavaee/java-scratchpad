public class Main {

  // Java 8 (with lambda expressions)
  public static void main(String[] args) {
    
    System.out.println(
      Maybe.just(1)
           .map((x) -> x + 20)
           .map((x) -> x * 2)
           .flatMap((x) -> {
             if (x != 42) return Maybe.nothing();
             else return Maybe.just(x);
           })
    );

    System.out.println(
      List.cons(1, List.cons(1, List.cons(2, List.cons(3, List.nil()))))
          .map((x) -> x + 20)
          .map((x) -> x * 2)
          .flatMap((x) -> {
            if (x != 42) return List.nil();
            else return List.cons(x, List.nil());
          })
    );

    System.out.println(
      new ListMonad<Integer>(java.util.Arrays.asList(1,1,2,3))
          .map((x) -> x + 20)
          .map((x) -> x * 2)
          .flatMap((x) -> {
            if (x != 42) return new ListMonad<Integer>(java.util.Arrays.asList());
            else return new ListMonad<Integer>(java.util.Arrays.asList(x));
          })
    );

    System.out.println(
      new State<java.util.List<String>,Integer>(
          new Fn1<java.util.List<String>,Tuple2<Integer,java.util.List<String>>>() {
            public Tuple2<Integer,java.util.List<String>> apply(java.util.List<String> log) {
              log.add("and one");
              return new Tuple2<Integer,java.util.List<String>>(1, log);
            }})
          .map((x) -> x + 20)
          .map((x) -> x * 2)
          .f.apply(new java.util.LinkedList())
          //.flatMap((x) -> {
          //  if (x != 42) return new ListMonad<Integer>(java.util.Arrays.asList());
          //  else return new ListMonad<Integer>(java.util.Arrays.asList(x));
          //})
    );

  }

}
