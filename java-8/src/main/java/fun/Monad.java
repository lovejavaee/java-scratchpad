package fun;

public interface Monad<A,M extends Monad<?,?>> extends Functor<A,M> {
  <B> M flatMap(Fn1<A,M> f);
}
