package fun;

public class LazyMaybe<A> implements Functor<A,LazyMaybe<?>> {

  private final Lazy<A> la;

  private LazyMaybe(Lazy<A> _la) {
    la = _la;
  }

  public <B> LazyMaybe<B> map(Fn1<A,B> f) {
    if (la == null) return nothing();
    else return new LazyMaybe<B>(new Lazy<B>(new Fn0<B>() { public B apply() { return f.apply(la.apply()); } } ) );
  }

  public String toString() {
    if (la == null || la.apply() == null) return "nothing";
    else return "just(" + la.apply().toString() + ")";
  }

  public static <A> LazyMaybe<A> just(A a) {
    return new LazyMaybe<A>(new Lazy<A>(a));
  }

  public static <A> LazyMaybe<A> just(Lazy<A> la) {
    return new LazyMaybe<A>(la);
  }

  public static <A> LazyMaybe<A> nothing() {
    return new LazyMaybe<A>(null);
  }

}
