package read;

import java.sql.Connection;
import java.sql.SQLException;

class JdbcAction<A> {

    private final Function1<Connection, A> g;

    JdbcAction(Function1<Connection, A> _g) {
        g = _g;
    }

    <B> JdbcAction<B> map(final Function1<A, B> h) {
        return new JdbcAction<B>(new Function1<Connection, B>() {
            public B apply(Connection c) throws SQLException {
                return h.apply(g.apply(c));
            }
        });
    }

    <B> JdbcAction<B> bind(final Function1<A, JdbcAction<B>> h) {
        return new JdbcAction<B>(new Function1<Connection, B>() {
            public B apply(Connection c) throws SQLException {
                return h.apply(g.apply(c)).apply(c);
            }
        });
    }

    A apply(Connection c) throws SQLException {
        return g.apply(c);
    }

    <B> JdbcAction<B> andThen(final JdbcAction<B> h) {
        return new JdbcAction<B>(new Function1<Connection, B>() {
            public B apply(Connection c) throws SQLException {
                g.apply(c);
                return h.apply(c);
            }
        });
    }

    static <A> JdbcAction<A> jdbcAction(Function1<Connection, A> g) {
        return new JdbcAction<A>(g);
    }

    static interface Function1<A, B> {
        B apply(A a) throws SQLException;
    }
}
