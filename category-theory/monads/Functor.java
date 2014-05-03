public interface Functor<A,F extends Functor<?,?>> {
  <B> F map(Fn1<A,B> f);
}
