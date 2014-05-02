package fun;

class Main {

  public static void justDemo() {
    Maybe<Integer> m1 = Maybe.just(1);
    Maybe<Integer> m2 = m1.map((x) -> x + 1);
    Maybe<Integer> m3 = m2.map((x) -> x + 3);
    System.out.println(m3.toString());
  }

  public static void nothingDemo() {
    Maybe<Integer> m1 = Maybe.nothing();
    Maybe<Integer> m2 = m1.map((x) -> x + 1);
    Maybe<Integer> m3 = m2.map((x) -> x + 3);
    System.out.println(m3.toString());
  }

  public static void lazyJustDemo() {
    LazyMaybe<Integer> m1 = LazyMaybe.just(1);
    LazyMaybe<Integer> m2 = m1.map((x) -> x + 1);
    LazyMaybe<Integer> m3 = m2.map((x) -> x + 3);
    System.out.println(m3.toString());
  }

  public static void lazyJustDemo2() {
    LazyMaybe<Integer> m1 = LazyMaybe.just(new Lazy<Integer>(1));

    Fn1<Integer,Integer> f1 =
      (x) -> {
        System.out.println("slowly adding 1...");
        try { Thread.sleep(1000); } catch (Exception e) { }
        return x + 1;
      };
    LazyMaybe<Integer> m2 = m1.map(f1);

    Fn1<Integer,Integer> f2 =
      (x) -> {
        System.out.println("slowly adding 3...");
        try { Thread.sleep(1000); } catch (Exception e) { }
        return x + 3;
      };

    LazyMaybe<Integer> m3 = m2.map(f2);
    System.out.println("calling LazyMaybe#toString()...");
    System.out.println(m3.toString());
  }

  public static void lazyNothingDemo() {
    LazyMaybe<Integer> m1 = LazyMaybe.nothing();
    LazyMaybe<Integer> m2 = m1.map((x) -> x + 1);
    LazyMaybe<Integer> m3 = m2.map((x) -> x + 3);
    System.out.println(m3.toString());
  }

  public static void main(String[] args) {
    justDemo();
    nothingDemo();
    lazyJustDemo();
    lazyNothingDemo();
    lazyJustDemo2();
  }

}
