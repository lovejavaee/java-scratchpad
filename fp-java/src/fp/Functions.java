package fp;

/**
 * Function interfaces of various arity.  Instances of these represent
 * functions, and are applied using the <tt>apply</tt> method.
 */
public class Functions {

    interface Fn0<A> {
        A apply();
    }

    interface Fn1<A, B> {
        B apply(A a);
    }

    interface Fn2<A, B, C> {
        C apply(A a, B b);
    }

    private static Fn0<Void> _unit = new Fn0<Void>() {
        @Override public Void apply() {
            return null;
        }
    };

    @SuppressWarnings("unchecked")//
    public static <A> Fn0<A> unit() {
        return (Fn0<A>) _unit;
    }

}
