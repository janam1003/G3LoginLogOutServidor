package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import thread.ExitThread;
import thread.WorkerThread;
import java.io.ObjectOutputStream;
import Classes.Message;
import Classes.MessageType;

/**
 *
 * @author Dani, Iñigo
 */
public class App {

	/**
	 * Counter of threads
	 */ 
	private static int contador = 0;
	/**
	 * Server socket for the connection
	 */
	private static ServerSocket servidor;
	/**
	 * Logger for the class
	 */
	private static final Logger logger = Logger.getLogger(App.class.getName());
	/**
	 * Port for the connection
	 */
	private static final int PORT = (Integer.parseInt(ResourceBundle.getBundle("config.config").getString("PORT")));
	/**
	 * Maximum number of users
	 */
	private static final int MAXUSERS = (Integer
			.parseInt(ResourceBundle.getBundle("config.config").getString("MAXUSERS")));
	/**
	 * Method to start the server
	 */
	public void iniciar() {

		try {
			logger.info("Server started.");
			// Creating server socket
			servidor = null;
			servidor = new ServerSocket(PORT);
			// Creating thread able to close the server
			ExitThread exitThread = new ExitThread();
			exitThread.start();
			// Loop to accept clients
			while (true) {
				Socket cliente = null;
				logger.info("Waiting for the client to connect");
				// Accepting client
				cliente = servidor.accept();
				logger.info("Client connected successfully");
				// Checking if the maximum number of users is reached
				if (countThreads(1, cliente) == -1) {
					// Sending message to the client that the server is full
					ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
					Message message = new Message();
					message.setType(MessageType.MAX_USER_EXCEPTION);
					oos.writeObject(message);
					oos.close();
					cliente.close();
				}
			}
		} catch (IOException e) {
			logger.severe("ERROR at input/output exception: " + e.getMessage());
		} catch (Exception e) {
			logger.severe("ERROR: " + e.getMessage());
		} finally {
			try {
				// Closing server socket
				servidor.close();
			} catch (Exception e) {
				logger.severe("ERROR closing ServerSocket " + e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		App server = new App();
		server.iniciar();
	}
	/**
	 * Method to count the clients connected synchronously
	 * @param x 1 to add a client, other to remove a client
	 * @param cliente Socket of the client
	 * @return 0 if the client can connect, -1 if the server is full
	 */
	public synchronized static int countThreads(int x, Socket cliente) {
    logger.info("Inside countThreads method.")
		if (x == 1) {
			// Checking if the maximum number of users is reached
			if (contador < MAXUSERS) {
				// Increasing the variable because a client has connected
				contador++;
				// Creating thread to handle the client
				WorkerThread worker = new WorkerThread(cliente);
				worker.start();
			} else {
				return -1;
			}
		} else {
			// Decreasing the variable because a client has disconnected
			contador -= 1;
		}
		return 0;
	}
}
