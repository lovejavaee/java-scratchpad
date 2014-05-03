public class ListFunctor<A> implements Functor<A,ListFunctor<?>> {

  public final java.util.List<A> l;

  public ListFunctor(java.util.List<A> _l) {
    l = _l;
  }

  public <B> ListFunctor<B> map(Fn1<A,B> f) {
    java.util.List<B> bs = new java.util.ArrayList<B>();
    for (A a : l) {
      bs.add(f.apply(a));
    }
    return new ListFunctor<B>(bs);
  }

  public String toString() {
    return l.toString();
  }

}
