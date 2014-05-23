public class List<A> implements Monad<A,List<?>> {

  private final A h;
  private final List<A> t;

  private List(A _h, List<A> _t) {
    h = _h;
    t = _t;
  }

  public <B> List<B> map(Fn1<A,B> f) {
    if (h == null) return nil();
    else if (t == null) return cons(f.apply(h), null);
    else return cons(f.apply(h), t.map(f));
  }

  public List<A> unit(A a) {
    return cons(a, nil());
  }

  @SuppressWarnings("unchecked")
  public <B> List<B> flatMap(Fn1<A,List<?>> f) {
    if (h == null) return nil();
    else if (t == null) return (List<B>)f.apply(h);
    else return concat((List<B>)f.apply(h), t.flatMap(f));
  }

  public String toString() {
    if (h == null) return "nil";
    else if (t == null) return "cons(" + h.toString() + ", nil)";
    else return "cons(" + h.toString() + ", " + t.toString() + ")";
  }

  public static <A> List<A> cons(A h, List<A> t) {
    return new List<A>(h, t);
  }

  public static <A> List<A> nil() {
    return new List<A>(null, null);
  }

  public static <A> List<A> concat(final List<A> xs, final List<A> ys) {
    if (xs.h != null) return new List<A>(xs.h, concat(xs.t, ys));
    else if (ys.h != null) return ys;
    else return nil();
  }

}
