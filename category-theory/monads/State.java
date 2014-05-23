public class State<S,A> implements Monad<A,State<?,?>> {

  public final Fn1<S,Tuple2<A,S>> f;

  public State(Fn1<S,Tuple2<A,S>> _f) {
    f = _f;
  }

  public <B> State<S,B> map(Fn1<A,B> g) {
    Fn1<S,Tuple2<B,S>> fb =
      new Fn1<S,Tuple2<B,S>>() {
        public Tuple2<B,S> apply(S s) {
          Tuple2<A,S> as = f.apply(s);
          B b = g.apply(as._1);
          Tuple2<B,S> bs = Tuple2.apply(b,as._2);
          return bs;
        }
    };
    return new State<S,B>(fb);
  }

  public State<S,A> unit(A a) {
    return apply(
      new Fn1<S,Tuple2<A,S>>() {
        public Tuple2<A,S> apply(S s) {
          return Tuple2.apply(a,s);
        }
      }
    );
   }

  @SuppressWarnings("unchecked")
  public <B> State<S,B> flatMap(Fn1<A,State<?,?>> g) {
    Fn1<S,Tuple2<B,S>> fb =
      new Fn1<S,Tuple2<B,S>>() {
        public Tuple2<B,S> apply(S s) {
          Tuple2<A,S> as = f.apply(s);
          State<S,B> sb = (State<S,B>)g.apply(as._1);
          Tuple2<B,S> bs = sb.f.apply(as._2);
          return bs;
        }
    };
    return new State<S,B>(fb);
  }

  public static <S,A> State<S,A> apply(Fn1<S,Tuple2<A,S>> _f) {
    return new State<S,A>(_f);
  }
}
