/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool;

/**
 *
 * @author Dani
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Stack;

public class Pool {

    private static final String URL = ResourceBundle.getBundle("config.config").getString("URL");
    private static final String USER = ResourceBundle.getBundle("config.config").getString("USER");
    private static final String PASSWORD = ResourceBundle.getBundle("config.config").getString("PASSWORD");

    private static Stack<Connection> connections = new Stack<>();

    public static synchronized Connection getConnection() throws SQLException {
        if (!connections.isEmpty()) {
            return connections.pop();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static synchronized void returnConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connections.push(connection);
        }
    }

    public static void closeAllConnections() throws SQLException {
        while (!connections.isEmpty()) {
            Connection connection = connections.pop();
            connection.close();
        }
    }
}
