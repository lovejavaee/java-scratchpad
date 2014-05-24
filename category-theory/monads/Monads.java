public class Monads {

  public static <M extends Monad<?,?>,A> M eta(A a, Monad<A,M> m) {
    return m.unit(a);
  }

}
