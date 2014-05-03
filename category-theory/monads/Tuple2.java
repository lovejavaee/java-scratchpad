public class Tuple2<A,B> {

  public final A _1;
  public final B _2;

  public Tuple2(A __1, B __2) {
    _1 = __1;
    _2 = __2;
  }

  public static <A,B> Tuple2<A,B> apply(A __1, B __2) {
    return new Tuple2<A,B>(__1, __2);
  }

  public String toString() {
    return "(" + _1.toString() + ", " + _2.toString() + ")";
  }
}
