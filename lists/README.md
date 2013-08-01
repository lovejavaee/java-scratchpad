# Exercise: Functional Linked Lists in Java

Given the interface `List`:

```java
interface List<A> {
  A head();
  List<A> tail();
}
```

and given the functions `odds` and `evens`:

```java
List<Integer> odds  = odds();  // 1 -> 3 -> 5 -> 7 -> ...
List<Integer> evens = evens(); // 2 -> 4 -> 6 -> 8 -> ...
```

Write the function `combine`, which returns in constant time:

```java
List<Integer> ints  = combine(odds, evens); // 1 -> 2 -> 3 -> 4 -> ...
```
