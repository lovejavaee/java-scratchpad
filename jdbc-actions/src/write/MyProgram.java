package write;

import static write.JdbcAction.jdbcAction;
import static write.JdbcAction.tuple;
import static write.JdbcAction.List.list;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import write.ConnectionProviders.HsqldbProvider;
import write.JdbcAction.Function1;
import write.JdbcAction.List;
import write.JdbcAction.Tuple2;

class MyProgram {

    public static void main(String[] args) {
        Connection c = HsqldbProvider.connection();
        JdbcAction<Void> myProgram = getName(c).map(println);
        Tuple2<Void, List<String>> result = new HsqldbProvider().apply(c, myProgram);
        System.out.println("logged: " + result.b);
    }

    static JdbcAction<String> getName(final Connection c) {
        return jdbcAction(new Function1<List<String>, Tuple2<String, List<String>>>() {
            public Tuple2<String, List<String>> apply(List<String> log) throws SQLException {
                Statement statement = c.createStatement();
                ResultSet resultset = statement.executeQuery("select * from person order by name");
                resultset.next();
                String name = resultset.getString("name");
                List<String> log2 = log.mappend(list("selected from `person` table: '" + name + "'"));
                return tuple(name, log2);
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