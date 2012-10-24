package read;

import static read.JdbcAction.jdbcAction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import read.ConnectionProviders.HsqldbProvider;
import read.JdbcAction.Function1;

class MyProgram {

    public static void main(String[] args) {
        JdbcAction<Void> myProgram = getName.map(println);
        new HsqldbProvider().apply(myProgram);
    }

    static JdbcAction<String> getName = jdbcAction(new Function1<Connection, String>() {
        public String apply(Connection c) throws SQLException {
            Statement statement = c.createStatement();
            ResultSet resultset = statement.executeQuery("select * from person order by name");
            resultset.next();
            return resultset.getString("name");
        }
    });

    static Function1<String, Void> println = new Function1<String, Void>() {
        public Void apply(String s) {
            System.out.println(s);
            return null;
        }
    };
}