package read;

import static read.JdbcAction.jdbcAction;
import static read.MyProgram.getName;
import static read.MyProgram.println;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import read.ConnectionProviders.HsqldbProvider;
import read.JdbcAction.Function1;

class MyProgram2 {

    public static void main(String[] args) {
        JdbcAction<Void> myProgram = addName("Jane Doe").andThen(getName.map(println));
        new HsqldbProvider().apply(myProgram);
    }

    static JdbcAction<Void> addName(final String name) {
        return jdbcAction(new Function1<Connection, Void>() {
            public Void apply(Connection c) throws SQLException {
                Statement statement = c.createStatement();
                statement.execute("insert into person values ('" + name + "')");
                return null;
            }
        });
    }
}