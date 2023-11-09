package pool;

import Exceptions.ServerErrorException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * This class represents a connection pool for managing database connections.
 *
 * @author Dani
 */
public class Pool {

    // Constants for the database connection details
    private static final String URL = ResourceBundle.getBundle("config.config").getString("URL");

    private static final String USER = ResourceBundle.getBundle("config.config").getString("USER");

    private static final String PASSWORD = ResourceBundle.getBundle("config.config").getString("PASSWORD");

    // A stack to hold available database connections
    private static Stack<Connection> connections = new Stack<>();

    private static final Logger logger = Logger.getLogger(Pool.class.getName());

    /**
     * Retrieve a database connection from the pool. If connections are
     * available in the pool, it will return one; otherwise, it creates a new
     * connection.
     *
     * @return A database connection
     * @throws ServerErrorException if a database connection cannot be
     * established
     */
    public static synchronized Connection getConnection() throws ServerErrorException {

        try {

            logger.info("Obtaining a database connection.");

            if (!connections.isEmpty()) {

                return connections.pop();

            }

            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {

            logger.severe("Unable to get a connection." + e.getMessage());

            throw new ServerErrorException("Unable to get a connection.");

        }
    }

    /**
     * Return a database connection to the pool for reuse.
     *
     * @param connection The database connection to be returned to the pool
     * @throws ServerErrorException if there is an issue with returning the
     * connection
     */
    public static synchronized void returnConnection(Connection connection) throws ServerErrorException {

        if (connection != null) {

            connections.push(connection);

            logger.info("Returning a database connection to the pool.");
        }
    }

    /**
     * Close all connections in the pool and clear the pool.
     *
     * @throws ServerErrorException if there is an issue with closing the
     * connections
     */
    public static void closeAllConnections() throws ServerErrorException {

        try {

            logger.info("Closing a database connection.");

            while (!connections.isEmpty()) {

                Connection connection = connections.pop();

                connection.close();
            }

        } catch (Exception e) {

            logger.severe("Unable to close every connection." + e.getMessage());

            throw new ServerErrorException("Unable to close every connection.");
        }
    }
}
