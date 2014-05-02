package fun;

public class Lazy<A> {

  private boolean evaluated = false;
  private A a;
  private final Fn0<A> fa;

  public Lazy(A _a) {
    a = _a;
    evaluated = true;
    fa = null;
  }

  public Lazy(Fn0<A> _fa) {
    fa = _fa;
  }

  public synchronized A apply() {
    if (evaluated) return a;
    else {
      a = fa.apply();
      evaluated = true;
      return a;
    }
  }

}
