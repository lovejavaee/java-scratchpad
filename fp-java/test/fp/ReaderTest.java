package fp;

import java.io.PrintStream;

import fp.Categories.Reader;
import fp.Functions.Fn1;

public class ReaderTest {

    public static class PSAction extends Reader<PrintStream, Void, PSAction> {

        private PSAction(Fn1<PrintStream, Void> f) {
            super(f);
        }

        public static <A> PSAction pure(final Fn1<PrintStream, A> f) {
            Fn1<PrintStream, Void> g = new Fn1<PrintStream, Void>() {
                @Override public Void apply(PrintStream ps) {
                    f.apply(ps);
                    return null;
                }
            };
            return new PSAction(g);
        }

        @Override public <A> PSAction lift(final Fn1<PrintStream, A> f) {
            return pure(f);
        }

        public PSAction andThen(final PSAction a) {
            Fn1<PrintStream, Void> g = new Fn1<PrintStream, Void>() {
                @Override public Void apply(PrintStream ps) {
                    PSAction.this.apply(ps);
                    a.apply(ps);
                    return null;
                }
            };
            return lift(g);
        }

    }

    public static PSAction foo = PSAction.pure(new Fn1<PrintStream, Void>() {
        @Override public Void apply(PrintStream ps) {
            ps.println("foo");
            return null;
        }
    });

    public static PSAction bar = PSAction.pure(new Fn1<PrintStream, Void>() {
        @Override public Void apply(PrintStream ps) {
            ps.println("bar");
            return null;
        }
    });

    public static void main(String[] args) {
        foo.andThen(bar).apply(System.out);
    }
}
