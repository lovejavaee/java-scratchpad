public interface Monad<A,M extends Monad<?,?>> extends Functor<A,M> {
  M unit(A a);
  <B> M flatMap(Fn1<A,M> f);
}
