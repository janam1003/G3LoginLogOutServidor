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
    private static final int MAXUSERS = (Integer.parseInt(ResourceBundle.getBundle("config.config").getString("MAXUSERS"))) ;

    private static Stack<Connection> connections = new Stack<>();

public static Connection getConnection() throws SQLException {
        if (connections.size() < MAXUSERS) {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connections.push(connection); 
        } else {
            throw new SQLException("No hay conexiones disponibles en el pool.");
        }
        return connections.pop();
    }

    public static void returnConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
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
