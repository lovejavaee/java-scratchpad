# Functional JDBC database interaction in Java

_26 Apr 2012_

This a collection of Java's take on warm, fuzzy things, to demonstrate environment injection and output accumulation.

## Reader

A reader encodes an action to take when given some environmental context.  A `JdbcAction<A>` encapsulates a function which, given a `Connection`, produces an instance of type `A`.  The encapsulated function implements the `Function1` interface:

```java
interface Function1<A, B> {
    B apply(A a) throws SQLException;
}
```

A `JdbcAction` wraps this function:

```java
class JdbcAction<A> {

    private final Function1<Connection, A> g;

    JdbcAction(Function1<Connection, A> _g) {
        g = _g;
    }

    A apply(Connection c) throws SQLException {
        return g.apply(c);
    }
```

Pure functions of type `A -> B` can be lifted into a `JdbcAction`:

```java
<B> JdbcAction<B> map(final Function1<A, B> h) {
    return new JdbcAction<B>(new Function1<Connection, B>() {
        public B apply(Connection c) throws SQLException {
            return h.apply(g.apply(c));
        }
    });
}

```

A function of type `A -> JdbcAction<B>` can be bound to a `JdbcAction<A>`:

```java
<B> JdbcAction<B> bind(final Function1<A, JdbcAction<B>> h) {
    return new JdbcAction<B>(new Function1<Connection, B>() {
        public B apply(Connection c) throws SQLException {
            return h.apply(g.apply(c)).apply(c);
        }
    });
}
```

A `JdbcAction` can be executed in serial with another `JdbcAction`:

```java
<B> JdbcAction<B> andThen(final JdbcAction<B> h) {
    return new JdbcAction<B>(new Function1<Connection, B>() {
        public B apply(Connection c) throws SQLException {
            g.apply(c);
            return h.apply(c);
        }
    });
}
```

[See here](https://en.wikipedia.org/wiki/Monad_%28functional_programming%29#Environment_monad) for more info.

## Writer

A writer encodes an action which produces some kind of composable output.  [See here](https://en.wikipedia.org/wiki/Monad_%28functional_programming%29#Writer_monad) for more info.

## ReaderWriter

Readers and writers can be useful in conjunction to encode actions, which produce some kind of composable output, to take when given some environmental context.
