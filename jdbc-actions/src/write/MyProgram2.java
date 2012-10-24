package write;

import static write.JdbcAction.jdbcAction;
import static write.JdbcAction.tuple;
import static write.JdbcAction.List.list;
import static write.MyProgram.getName;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import write.ConnectionProviders.HsqldbProvider;
import write.JdbcAction.Function1;
import write.JdbcAction.List;
import write.JdbcAction.Tuple2;

class MyProgram2 {

    public static void main(String[] args) {
        Connection c = HsqldbProvider.connection();
        JdbcAction<Void> myProgram = addName("Jane Doe", c).andThen(getName(c).map(println));
        Tuple2<Void, List<String>> result = new HsqldbProvider().apply(c, myProgram);
        System.out.println("logged: " + result.b);
    }

    static JdbcAction<Void> addName(final String name, final Connection c) {
        return jdbcAction(new Function1<List<String>, Tuple2<Void, List<String>>>() {
            public Tuple2<Void, List<String>> apply(List<String> log) throws SQLException {
                Statement statement = c.createStatement();
                statement.execute("insert into person values ('" + name + "')");
                List<String> log2 = log.mappend(list("inserted into `person` table: '" + name + "'"));
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