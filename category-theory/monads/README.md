# Monads in Java

*6 May 2014*

In category theory, a monad builds on a [certain morphism](https://github.com/earldouglas/java-scratchpad/tree/master/category-theory/functors#functors-in-java) between categories.  Practically speaking, given the following unary function interface:

```java
public interface Fn1<A,B> {
  B apply(A a);
}
```

A function of type `Fn1<A,F<B>>` to be lifted to a function of type `Fn1<F<A>,F<B>>`, for some generic type `F`.

In Java, a monad might look like this:

```java
public interface Functor<A,F extends Functor<?,?>> {
  <B> F map(Fn1<A,B> f);
}

public interface Monad<A,M extends Monad<?,?>> extends Functor<A,M> {
  <B> M flatMap(Fn1<A,M> f);
}
```

I like to picture it like this:

```
.--------------.         .-------------------.
|  Category *  |         |   Category F<*>   |
|--------------|         |-------------------|
| A            |         |   F<A>            |
| B            |         |   F<B>            |
| A  ======================> F<B>            |
|           |  |         |                   |
|           '~~~ flatMap ~~> Fn1<F<A>,F<B>>  |
'--------------'         '-------------------'
```

This can be read as follows:

1. The category `*` contains objects of type `A` and `B`, and a morphism from `A` to `F<B>`, which is in the category `F<*>`.
2. The category `F<*>` contains objects of type `F<A>` and `F<B>`, and a morphism from `F<A>` to `F<B>`.
3. A monad for `F<*>` converts the `Fn1<A,F<B>>` morphism into the `Fn1<F<A>,F<B>>` morphism via `flatMap`.

This is useful when you have a value of type `F<A>` and you want to apply a function of type `Fn1<A,F<B>>` to it, resulting in a value of type `F<B>`.

Though Java doesn't have an explicit unary function interface, a `Functor` interface, or a `Monad` interface, it's easy to write simple data structures that make use of them.  Let's look at some examples.

## Maybe

In other languages, a `Maybe` is a box that might contain a value.  It has two implementations: `Just`, which contains a value of a given type, and `Nothing`, which does not contain a value.

If we imagine that `Maybe` represents the category of optional values, we can see that `Just` and `Nothing` are monads.  Here's how it looks:

```
.------------------.         .--------------------------------------.
|  Category *      |         |        Category Maybe<*>             |
|------------------|         |--------------------------------------|
| Int              |         |   Maybe<Integer>                     |
| String           |         |   Maybe<String>                      |
| String  =====================> Maybe<Integer>                     |
|               |  |         |                                      |
|               '~~~ flatMap ~~> Fn1<Maybe<String>,Maybe<Integer>>  |
'------------------'         '--------------------------------------'
```

In Java, we can implement `Maybe` as a single class that abstracts over `Just` and `Nothing`:

```java
public class Maybe<A> implements Functor<A,Maybe<?>> {

  private final A a;

  private Maybe(A _a) {
    a = _a;
  }

  public <B> Maybe<B> map(Fn1<A,B> f) {
    if (a == null) return nothing();
    else return just(f.apply(a));
  }

  @SuppressWarnings("unchecked")
  public <B> Maybe<B> flatMap(Fn1<A,Maybe<?>> f) {
    if (a == null) return nothing();
    else return (Maybe<B>)f.apply(a);
  }

  public String toString() {
    if (a == null) return "nothing";
    else return "just(" + a.toString() + ")";
  }

  public static <A> Maybe<A> just(A a) {
    return new Maybe<A>(a);
  }

  public static <A> Maybe<A> nothing() {
    return new Maybe<A>(null);
  }

}
```

We can try it out on some simple lambdas:

```java
System.out.println(
  Maybe.just(1)
       .map((x) -> x + 20)
       .map((x) -> x * 2)
       .flatMap((x) -> {
         if (x != 42) return Maybe.nothing();
         else return Maybe.just(x);
       })
); // just(42)
```

The expression `(x) -> x + 20` is syntatic sugar in Java 8 for a `Fn1<Integer,Integer>` instance, since it is a class with exactly one method:

```java
new Fn1<Integer,Integer>() {
  public Integer apply(Integer x) {
    return x + 20;
  }
}
``` 

When using `map` on `just(1)`, we start with an instance of type `Maybe<Integer>` and apply the function `(x) -> x + 20` of type `Fn1<Int,Int>`, resulting in an instance of type `Maybe<Integer>` whose value `a` is `21`.

When we then use `map` to apply the function `(x) -> x * 2`, we end up with a `Maybe<Integer>` whose value `a` is `42`.

When we finally use `flatMap` to apply the function `(x) -> if (x != 42) return Maybe.nothing() else return Maybe.just(x)`, we end up with a `Maybe<Integer>` whose value `a` is `42`.

Since `Maybe<A>` implements a method `<B> Maybe<B> flatMap(Fn1<A,Maybe<?>> f)`, `Maybe` is a monad.

## List

In other languages, a `List` is a box that contains zero or more values in a particular order.  Like `Maybe`, it has two implementations: `Cons`, which contains both a value of a given type and a subsequent `List`, and `Nil`, which contains neither a value nor a subsequent `List`.

If we imagine that `List` represents the category of lists of values, we can see that it is also a monad.  Here's how it looks:


```
.------------------.         .------------------------------------.
|  Category *      |         |       Category List<*>             |
|------------------|         |------------------------------------|
| Int              |         |   List<Integer>                    |
| String           |         |   List<String>                     |
| String  =====================> List<Integer>                    |
|               |  |         |                                    |
|               '~~~ flatMap ~~> Fn1<List<String>,List<Integer>>  |
'------------------'         '------------------------------------'
```

In Java, we can implement `List` as a single class that abstracts over `Cons` and `Nil`:

```java
public class List<A> implements Monad<A,List<?>> {

  private final A h;
  private final List<A> t;

  private List(A _h, List<A> _t) {
    h = _h;
    t = _t;
  }

  public <B> List<B> map(Fn1<A,B> f) {
    if (h == null) return nil();
    else if (t == null) return cons(f.apply(h), null);
    else return cons(f.apply(h), t.map(f));
  }

  @SuppressWarnings("unchecked")
  public <B> List<B> flatMap(Fn1<A,List<?>> f) {
    if (h == null) return nil();
    else if (t == null) return (List<B>)f.apply(h);
    else return concat((List<B>)f.apply(h), t.flatMap(f));
  }

  public String toString() {
    if (h == null) return "nil";
    else if (t == null) return "cons(" + h.toString() + ", nil)";
    else return "cons(" + h.toString() + ", " + t.toString() + ")";
  }

  public static <A> List<A> cons(A h, List<A> t) {
    return new List<A>(h, t);
  }

  public static <A> List<A> nil() {
    return new List<A>(null, null);
  }

}
```

As before, we can try it out on some simple lambdas:

```java
System.out.println(
  List.cons(1, List.cons(1, List.cons(2, List.cons(3, List.nil()))))
      .map((x) -> x + 20)
      .map((x) -> x * 2)
      .flatMap((x) -> {
        if (x != 42) return List.nil();
        else return List.cons(x, List.nil());
      })
); // cons(42, cons(42, nil))
```

When using `map` on `cons(1, ...)`, we start with an instance of type `List<Integer>`, apply the function `(x) -> x + 20` of type `Fn1<Int,Int>`, resulting in an instance of type `List<Integer>` representing `[21, 21, 22, 23]`.

When we then use `map` to apply the function `(x) -> x * 2`, we end up with a `List<Integer>` representing `[42, 42, 44, 46]`.

When we finally use `flatMap` to apply the function `(x) -> if (x != 42) return List.nil() else return List.cons(x, List.nil()`, we end up with a `List<Integer>` representing `[42, 42]`.

Since `List<A>` implements a method `<B> List<B> flatMap(Fn1<A,List<?>> f)`, `List` is a monad.

# java.util.List

Though Java's built-in `List` interface doesn't extend the `Monad` interface, it's easy to write a simple wrapper that gets us most of the way there.

```java
public class ListMonad<A> implements Monad<A,ListMonad<?>> {

  public final java.util.List<A> l;

  public ListMonad(java.util.List<A> _l) {
    l = _l;
  }

  public <B> ListMonad<B> map(Fn1<A,B> f) {
    java.util.List<B> bs = new java.util.ArrayList<B>();
    for (A a : l) {
      bs.add(f.apply(a));
    }
    return new ListMonad<B>(bs);
  }

  @SuppressWarnings("unchecked")
  public <B> ListMonad<B> flatMap(Fn1<A,ListMonad<?>> f) {
    java.util.List<B> bs = new java.util.ArrayList<B>();
    for (A a : l) {
      bs.addAll(((ListMonad<B>)f.apply(a)).l);
    }
    return new ListMonad<B>(bs);
  }

  public String toString() {
    return l.toString();
  }

}
```

We can use this in a similar fashion to our `List` implementation:

```java
System.out.println(
  ListMonad.<Integer>apply(java.util.Arrays.asList(1,1,2,3))
           .map((x) -> x + 20)
           .map((x) -> x * 2)
           .flatMap((x) -> {
             if (x != 42) return new ListMonad<Integer>(java.util.Arrays.asList());
             else return new ListMonad<Integer>(java.util.Arrays.asList(x));
           })
); // [42, 42]
```

Since `ListMonad<A>` implements a method `<B> ListMonad<B> flatMap(Fn1<A,ListMonad<?>> f)`, `ListMonad` is a monad.

# State

We can even write monads for classes with two generic type parameters by fixing one of them outside the context of the monad implementation.  Let's look at `State`, which gives us a way to tranpsarently thread a reference through a sequence of function calls.

```java
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
```

Here's an example where we pass a log of type `java.util.List<String>` along through a few computations:

```java
System.out.println(
  State.<java.util.List<String>,Integer>apply(
         (log) -> {
           log.add("and one");
           return new Tuple2<Integer,java.util.List<String>>(1, log);
         }
       )
       .map((x) -> x + 20)
       .map((x) -> x * 2)
       .flatMap((x) ->
         State.<java.util.List<String>,Integer>apply(
                (log) -> {
                  if (x != 42) {
                    log.add("boo");
                    return Tuple2.<Integer,java.util.List<String>>apply(-999, log);
                  } else {
                    log.add("yay");
                    return Tuple2.<Integer,java.util.List<String>>apply(x, log);
                  }
                }
              )
       )
       .f.apply(new java.util.LinkedList<String>())
); // (42, [and one, yay])
```

Since `State<A>` implements a method `<B> State<B> flatMap(Fn1<A,State<?>> f)`, `State` is a monad.
