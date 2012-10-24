package readwrite;

import static readwrite.JdbcAction.jdbcAction;
import static readwrite.JdbcAction.tuple;
import static readwrite.JdbcAction.List.list;
import static readwrite.MyProgram.getName;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import readwrite.ConnectionProviders.HsqldbProvider;
import readwrite.JdbcAction.Function1;
import readwrite.JdbcAction.List;
import readwrite.JdbcAction.Tuple2;

class MyProgram2 {

    public static void main(String[] args) {
        JdbcAction<Void> myProgram = addName("Jane Doe").andThen(getName.map(println));
        List<String> log = new HsqldbProvider().apply(myProgram).b;
        System.out.println("logged: " + log);
    }

    static JdbcAction<Void> addName(final String name) {
        return jdbcAction(new Function1<Tuple2<Connection, List<String>>, Tuple2<Void, List<String>>>() {
            public Tuple2<Void, List<String>> apply(Tuple2<Connection, List<String>> state) throws SQLException {
                Statement statement = state.a.createStatement();
                statement.execute("insert into person values ('" + name + "')");
                List<String> log2 = state.b.mappend(list("inserted into `person` table: '" + name + "'"));
                return tuple(null, log2);
            }
        });
    }

    static Function1<String, Void> println = new Function1<String, Void>() {
        public Void apply(String s) {
            System.out.println(s);
            return null;
        }
    };
}