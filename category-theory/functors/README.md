# Functors in Java

*3 May 2014*

In category theory, a functor is a mapping between categories.  Practically speaking, given the following unary function interface:

```java
public interface Fn1<A,B> {
  B apply(A a);
}
```

A function of type `Fn1<A,B>` to be lifted to a function of type `Fn1<F<A>,F<B>>`, for some generic type `F`.


In Java, a functor might look like this:

```java
public interface Functor<A,F extends Functor<?,?>> {
  <B> F map(Fn1<A,B> f);
}
```

I like to picture it like this:

```
.--------------.         .-------------------.
|  Category *  |         |   Category F<*>   |
|--------------|         |-------------------|
| A            |         |   F<A>            |
| B            |         |   F<B>            |
| Fn1<A,B> ~~~~~~~ map ~~~~> Fn1<F<A>,F<B>>  |
'--------------'         '-------------------'
```

This can be read as follows:

1. The category `*` contains objects of type `A` and `B`, and a morphism from `A` to `B`.
2. The category `F<*>` contains objects of type `F<A>` and `F<B>`, and a morphism from `F<A>` to `F<B>`.
3. A functor for `F<*>` maps morphisms from `*`, for example converting `Fn1<A,B>` into `Fn1<F<A>,F<B>>`.

This is useful when you have a value of type `F<A>` and you want to apply a function of type `Fn1<A,B>` to it, resulting in a value of type `F<B>`.

Though Java doesn't have either an explicit unary function interface or a `Functor` interface, it's easy to write simple data structures that make use of both.  Let's look at some examples.

## Maybe

In other languages, a `Maybe` is a box that might contain a value.  It has two implementations: `Just`, which contains a value of a given type, and `Nothing`, which does not contain a value.

If we imagine that `Maybe` represents the category of optional values, we can see that `Just` and `Nothing` are functors.  Here's how it looks:


```
.------------------.         .--------------------------------------.
|  Category *      |         |        Category Maybe<*>             |
|------------------|         |--------------------------------------|
| Int              |         |   Maybe<Integer>                     |
| String           |         |   Maybe<String>                      |
| Fn1<Int,Int>    ~~~~ map ~~~~> Fn1<Maybe<Integer>,Maybe<Integer>> |
| Fn1<Int,String> ~~~~ map ~~~~> Fn1<Maybe<Integer>,Maybe<String>>  |
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
); // just(42)

System.out.println(
  Maybe.<Integer>nothing()
       .map((x) -> x + 20)
       .map((x) -> x * 2)
); // nothing
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

Since `Maybe<A>` implements a method `<B> Maybe<B> map(Fn1<A,B> f)`, `Maybe` is a functor.

## List

In other languages, a `List` is a box that contains zero or more values in a particular order.  Like `Maybe`, it has two implementations: `Cons`, which contains both a value of a given type and a subsequent `List`, and `Nil`, which contains neither a value nor a subsequent `List`.

If we imagine that `List` represents the category of lists of values, we can see that it is also a functor.  Here's how it looks:


```
.------------------.         .------------------------------------.
|  Category *      |         |       Category List<*>             |
|------------------|         |------------------------------------|
| Int              |         |   List<Integer>                    |
| String           |         |   List<String>                     |
| Fn1<Int,Int>    ~~~~ map ~~~~> Fn1<List<Integer>,List<Integer>> |
| Fn1<Int,String> ~~~~ map ~~~~> Fn1<List<Integer>,List<String>>  |
'------------------'         '------------------------------------'
```

In Java, we can implement `List` as a single class that abstracts over `Cons` and `Nil`:

```java
public class List<A> implements Functor<A,List<?>> {

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
); // cons(42, cons(42, cons(44, cons(46, nil))))

System.out.println(
  List.<Integer>nil()
      .map((x) -> x + 20)
      .map((x) -> x * 2)
); // nil
```

When using `map` on `cons(1, ...)`, we start with an instance of type `List<Integer>`, apply the function `(x) -> x + 20` of type `Fn1<Int,Int>`, resulting in an instance of type `List<Integer>` representing `[21, 21, 22, 23]`.

When we then use `map` to apply the function `(x) -> x * 2`, we end up with a `List<Integer>` representing `[42, 42, 44, 46]`.

Since `List<A>` implements a method `<B> List<B> map(Fn1<A,B> f)`, `List` is a functor.

# java.util.List

Though Java's built-in `List` interface doesn't extend the `Functor` interface, it's easy to write a simple wrapper that gets us most of the way there.

```java
public class ListFunctor<A> implements Functor<A,ListFunctor<?>> {

  public final java.util.List<A> l;

  public ListFunctor(java.util.List<A> _l) {
    l = _l;
  }

  public <B> ListFunctor<B> map(Fn1<A,B> f) {
    java.util.List<B> bs = new java.util.ArrayList<B>();
    for (A a : l) {
      bs.add(f.apply(a));
    }
    return new ListFunctor<B>(bs);
  }

  public String toString() {
    return l.toString();
  }

}
```

We can use this in a similar fashion to our `List` implementation:

```java
System.out.println(
  new ListFunctor<Integer>(java.util.Arrays.asList(1,1,2,3))
    .map((x) -> x + 20)
    .map((x) -> x * 2)
  ); // [42, 42, 44, 46]

System.out.println(
  new ListFunctor<Integer>(new java.util.LinkedList<Integer>())
    .map((x) -> x + 20)
    .map((x) -> x * 2)
  ); // []
```

Since `ListFunctor<A>` implements a method `<B> ListFunctor<B> map(Fn1<A,B> f)`, `ListFunctor` is a functor.
