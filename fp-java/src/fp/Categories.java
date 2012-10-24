package fp;

import fp.Functions.Fn1;

/**
 * A place to store various structures inspired by category theory.  This
 * includes functors, applicatives, monads, etc.
 */
public class Categories {

    public static interface Functor<A, F extends Functor<?, ?>> {
        <B> F map(Fn1<A, B> f);
    }

    public static interface Monad<A, M extends Monad<?, ?>> extends Functor<A, M> {
        <B> M flatMap(Fn1<A, M> f);
    }

    public static abstract class Reader<C, A, R extends Reader<?, ?, ?>> implements Monad<A, R> {

        private final Fn1<C, A> f;

        protected Reader(Fn1<C, A> _f) {
            f = _f;
        }

        public abstract <AA> R lift(Fn1<C, AA> _f);

        public A apply(C c) {
            return f.apply(c);
        }

        @Override public <B> R map(final Fn1<A, B> g) {
            Fn1<C, B> h = new Fn1<C, B>() {
                @Override public B apply(C c) {
                    return g.apply(f.apply(c));
                }
            };
            return lift(h);
        }

        @SuppressWarnings("unchecked")//
        @Override public <B> R flatMap(final Fn1<A, R> g) {
            Fn1<C, B> h = new Fn1<C, B>() {
                @Override public B apply(C c) {
                    Reader<C, B, ?> r = (Reader<C, B, ?>) g.apply(f.apply(c));
                    return r.apply(c);
                }
            };
            return lift(h);
        }
    }
}
