/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool;

/**
 * This class represents a connection pool for managing database connections.
 *
 * @author Dani
 */
import Exceptions.ServerErrorException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Stack;

public class Pool {

    // Constants for the database connection details
    private static final String URL = ResourceBundle.getBundle("config.config").getString("URL");
    private static final String USER = ResourceBundle.getBundle("config.config").getString("USER");
    private static final String PASSWORD = ResourceBundle.getBundle("config.config").getString("PASSWORD");

    // A stack to hold available database connections
    private static Stack<Connection> connections = new Stack<>();

    /**
     * Retrieve a database connection from the pool. If connections are available in the pool, 
     * it will return one; otherwise, it creates a new connection.
     *
     * @return A database connection
     * @throws ServerErrorException if a database connection cannot be established
     */
    public static synchronized Connection getConnection() throws ServerErrorException {
        try {
        if (!connections.isEmpty()) {
            return connections.pop();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }catch(SQLException e ){
     throw new ServerErrorException("Unable to get a connection");
    }
    }

    /**
     * Return a database connection to the pool for reuse.
     *
     * @param connection The database connection to be returned to the pool
     * @throws ServerErrorException if there is an issue with returning the connection
     */
    public static synchronized void returnConnection(Connection connection) throws ServerErrorException {
        if (connection != null) {
            connections.push(connection);
        }
    }

    /**
     * Close all connections in the pool and clear the pool.
     *
     * @throws ServerErrorException if there is an issue with closing the connections
     */
    public static void closeAllConnections() throws ServerErrorException {
        try{
        while (!connections.isEmpty()) {
            Connection connection = connections.pop();
            connection.close();
        }
        }catch(SQLException e){
      throw new ServerErrorException("Unable to close every connection");
    }
    }
}
