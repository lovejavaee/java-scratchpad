package fp;

import static fp.Functions.unit;
import fp.Categories.Functor;
import fp.Categories.Monad;
import fp.Functions.Fn0;
import fp.Functions.Fn1;

public class FreeMonads {

    public static abstract class Free<A, F extends Free<?, ?>> implements Monad<A, Free<?, ?>> {

        @SuppressWarnings("unchecked")//
        public <B> Free<B, F> map(final Fn1<A, B> f) {
            Fn1<A, Free<?, ?>> g = new Fn1<A, Free<?, ?>>() {
                public Free<B, ?> apply(A a) {
                    return (Free<B, ?>) f.apply(a);
                }
            };
            return (Free<B, F>) flatMap(g);
        }

        @SuppressWarnings("unchecked")//
        public <B> Free<B, F> flatMap(Fn1<A, Free<?, ?>> f) {
            if (this instanceof Done) {
                A a = ((Done<A>) this).a;
                return (Free<B, F>) f.apply(a);
            } else if (this instanceof More) {
                Functor<A, ?> k = ((More<A, ?>) this).k;
                return (Free<B, F>) k.map(f);
            } else {
                throw new RuntimeException(getClass().getName() + " is not an instance of Free");
            }
        }
    }

    public static class Done<A> extends Free<A, Free<?, ?>> {
        public final A a;

        public Done(A _a) {
            a = _a;
        }
    }

    public static Free<Fn0<Void>, Free<?, ?>> done() {
        Fn0<Void> unit = unit();
        return new Done<Fn0<Void>>(unit);
    }

    public static class More<A, F extends Free<A, ?>> extends Free<A, F> {
        public final F k;

        public More(F _k) {
            k = _k;
        }
    }

    public static <A, F extends Free<A, ?>> Free<A, F> more(F f) {
        return new More<A, F>(f);
    }

}
