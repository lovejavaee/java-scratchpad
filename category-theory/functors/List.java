public class List<A> implements Functor<A,List<?>> {

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

}
