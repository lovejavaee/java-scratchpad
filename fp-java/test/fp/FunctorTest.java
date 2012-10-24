package fp;

import fp.Categories.Functor;
import fp.Functions.Fn1;

public class FunctorTest {

    public static class Box<A> implements Functor<A, Box<?>> {

        public final A a;

        public Box(A _a) {
            a = _a;
        }

        public <B> Box<B> map(Fn1<A, B> f) {
            return new Box<B>(f.apply(a));
        }

        public String toString() {
            return "Box<" + a.getClass().getSimpleName() + ">(" + a + ")";
        }
    }

    public static void main(String[] args) {
        Box<Integer> b1 = new Box<Integer>(123);
        System.out.println("b1: " + b1.toString());
        Box<String> b2 = b1.map(new Fn1<Integer, String>() {
            public String apply(Integer a) {
                return a.toString();
            }
        });
        System.out.println("b2: " + b2.toString());
    }
}
