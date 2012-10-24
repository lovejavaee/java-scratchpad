package readwrite;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

class JdbcAction<A> {

    private final Function1<Tuple2<Connection, List<String>>, Tuple2<A, List<String>>> g;

    JdbcAction(Function1<Tuple2<Connection, List<String>>, Tuple2<A, List<String>>> _g) {
        g = _g;
    }

    <B> JdbcAction<B> map(final Function1<A, B> h) {
        return new JdbcAction<B>(new Function1<Tuple2<Connection, List<String>>, Tuple2<B, List<String>>>() {
            public Tuple2<B, List<String>> apply(Tuple2<Connection, List<String>> state) throws SQLException {
                Tuple2<A, List<String>> x = g.apply(state);
                B b = h.apply(x.a);
                return tuple(b, state.b.mappend(x.b));
            }
        });
    }

    <B> JdbcAction<B> bind(final Function1<A, JdbcAction<B>> h) {
        return new JdbcAction<B>(new Function1<Tuple2<Connection, List<String>>, Tuple2<B, List<String>>>() {
            public Tuple2<B, List<String>> apply(Tuple2<Connection, List<String>> state) throws SQLException {
                Tuple2<A, List<String>> result1 = g.apply(state);
                Tuple2<B, List<String>> result2 = h.apply(result1.a).apply(state.a);
                return tuple(result2.a, result1.b.mappend(result2.b));
            }
        });
    }

    Tuple2<A, List<String>> apply(Connection c) throws SQLException {
        return g.apply(tuple(c, new List<String>()));
    }

    <B> JdbcAction<B> andThen(final JdbcAction<B> h) {
        return new JdbcAction<B>(new Function1<Tuple2<Connection, List<String>>, Tuple2<B, List<String>>>() {
            public Tuple2<B, List<String>> apply(Tuple2<Connection, List<String>> state) throws SQLException {
                Tuple2<A, List<String>> result1 = g.apply(state);
                Tuple2<B, List<String>> result2 = h.apply(state.a);
                return tuple(result2.a, result1.b.mappend(result2.b));
            }
        });
    }

    static <A> JdbcAction<A> jdbcAction(Function1<Tuple2<Connection, List<String>>, Tuple2<A, List<String>>> g) {
        return new JdbcAction<A>(g);
    }

    static interface Function0<A> {
        A apply() throws SQLException;
    }

    static interface Function1<A, B> {
        B apply(A a) throws SQLException;
    }

    static <A, B> Tuple2<A, B> tuple(A a, B b) {
        return new Tuple2<A, B>(a, b);
    }

    static class Tuple2<A, B> {
        final A a;
        final B b;

        Tuple2(A _a, B _b) {
            a = _a;
            b = _b;
        }
    }

    @SuppressWarnings("serial")
    static class List<A> extends LinkedList<A> {

        static <A> List<A> list(A a) {
            List<A> list = new List<A>();
            list.add(a);
            return list;
        }

        List<A> mempty() {
            return new List<A>();
        }

        List<A> mappend(List<A> x) {
            List<A> y = new List<A>();
            y.addAll(this);
            y.addAll(x);
            return y;
        }
    }
}
