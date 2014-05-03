public class Maybe<A> implements Functor<A,Maybe<?>> {

  private final A a;

  private Maybe(A _a) {
    a = _a;
  }

  public <B> Maybe<B> map(Fn1<A,B> f) {
    if (a == null) return nothing();
    else return just(f.apply(a));
  }

  public String toString() {
    if (a == null) return "nothing";
    else return "just(" + a.toString() + ")";
  }

  public static <A> Maybe<A> just(A a) {
    return new Maybe<A>(a);
  }

  public static <A> Maybe<A> nothing() {
    return new Maybe<A>(null);
  }

}
