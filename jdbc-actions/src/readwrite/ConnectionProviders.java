package readwrite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import readwrite.JdbcAction.List;
import readwrite.JdbcAction.Tuple2;

class ConnectionProviders {

    static interface ConnectionProvider {
        <A> Tuple2<A, List<String>> apply(JdbcAction<A> jdbcAction);
    }

    static class HsqldbProvider implements ConnectionProvider {

        public <A> Tuple2<A, List<String>> apply(JdbcAction<A> jdbcAction) {
            Connection connection = connection();
            try {
                // in-mem database; need to create tables
                connection.createStatement().execute("create table person (name varchar(256))");

                // in-mem database; load initial data
                connection.createStatement().execute("insert into person values ('John Doe')");

                Tuple2<A, List<String>> state = jdbcAction.apply(connection);
                connection.commit();

                return state;

            } catch (SQLException e) {
                rollback(connection);
                throw new RuntimeException(e);
            } finally {
                close(connection);
            }
        }

        private void close(Connection connection) {
            try {
                connection.createStatement().execute("shutdown");
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static void rollback(Connection connection) {
            try {
                connection.rollback();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static Connection connection() {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                String url = "jdbc:hsqldb:mem:jdbcactions";
                String username = "sa";
                String password = "";
                Connection connection = DriverManager.getConnection(url, username, password);
                connection.setAutoCommit(false);
                return connection;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}