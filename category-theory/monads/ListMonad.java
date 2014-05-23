public class ListMonad<A> implements Monad<A,ListMonad<?>> {

  public final java.util.List<A> l;

  public ListMonad(java.util.List<A> _l) {
    l = _l;
  }

  public <B> ListMonad<B> map(Fn1<A,B> f) {
    java.util.List<B> bs = new java.util.ArrayList<B>();
    for (A a : l) {
      bs.add(f.apply(a));
    }
    return new ListMonad<B>(bs);
  }

  public ListMonad<A> unit(A a) {
    return apply(java.util.Arrays.asList(a));
  }

  @SuppressWarnings("unchecked")
  public <B> ListMonad<B> flatMap(Fn1<A,ListMonad<?>> f) {
    java.util.List<B> bs = new java.util.ArrayList<B>();
    for (A a : l) {
      bs.addAll(((ListMonad<B>)f.apply(a)).l);
    }
    return new ListMonad<B>(bs);
  }

  public String toString() {
    return l.toString();
  }

  public static <A> ListMonad<A> apply(java.util.List<A> _l) {
    return new ListMonad<A>(_l);
  }
}
