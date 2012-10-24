package readwrite;

import static readwrite.JdbcAction.jdbcAction;
import static readwrite.JdbcAction.tuple;
import static readwrite.JdbcAction.List.list;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import readwrite.ConnectionProviders.HsqldbProvider;
import readwrite.JdbcAction.Function1;
import readwrite.JdbcAction.List;
import readwrite.JdbcAction.Tuple2;

class MyProgram {

    public static void main(String[] args) {
        JdbcAction<Void> myProgram = getName.map(println);
        List<String> log = new HsqldbProvider().apply(myProgram).b;
        System.out.println("logged: " + log);
    }

    static JdbcAction<String> getName = jdbcAction(new Function1<Tuple2<Connection, List<String>>, Tuple2<String, List<String>>>() {
        public Tuple2<String, List<String>> apply(Tuple2<Connection, List<String>> state) throws SQLException {
            Statement statement = state.a.createStatement();
            ResultSet resultset = statement.executeQuery("select * from person order by name");
            resultset.next();
            String name = resultset.getString("name");
            List<String> log2 = state.b.mappend(list("selected from `person` table: '" + name + "'"));
            return tuple(name, log2);
        }
    });

    static Function1<String, Void> println = new Function1<String, Void>() {
        public Void apply(String s) {
            System.out.println(s);
            return null;
        }
    };
}