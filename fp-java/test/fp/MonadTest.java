package fp;

import fp.Categories.Monad;
import fp.Functions.Fn1;

public class MonadTest {

    public static class Box<A> implements Monad<A, Box<?>> {

        public final A a;

        public Box(A _a) {
            a = _a;
        }

        public String toString() {
            return "Box<" + a.getClass().getSimpleName() + ">(" + a + ")";
        }

        public <B> Box<B> map(Fn1<A, B> f) {
            return new Box<B>(f.apply(a));
        }

        @SuppressWarnings("unchecked")//
        public <B> Box<B> flatMap(Fn1<A, Box<?>> f) {
            return (Box<B>) f.apply(a);
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

        Fn1<Integer, Box<?>> h = new Fn1<Integer, Box<?>>() {
            public Box<String> apply(Integer a) {
                return new Box<String>(a.toString());
            }
        };
        Box<String> b3 = b1.flatMap(h);
        System.out.println("b3: " + b3.toString());
    }
}
