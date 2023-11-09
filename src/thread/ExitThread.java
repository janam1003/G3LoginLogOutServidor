package thread;

import java.util.logging.Logger;
import pool.Pool;
/*
 * This class manages the thread to exit the application.
 */
public class ExitThread extends Thread {

  /*
	 * Logger Object.
	 */
   private static final Logger logger = Logger.getLogger(ExitThread.class.getName());
  
	/*
	 * This method manages the thread to exit the application.
	 */
	@Override
	public void run() {
		try {

			while (true) {
         logger.info("WRITE '1' TO CLOSE THE SERVER.\n");
				// The thread will wait for the user to press a key
				int input = System.in.read();
				// If the user presses '1', the server will close
				if (input == '1') {
           logger.info("Received exit signal. Closing all connections and exiting.");
					// Closing all the connections on the pool
					Pool.closeAllConnections();
					// Closing the server
					System.exit(0);
				}
			}
		} catch (Exception e) {
			logger.severe("Error: " + e.getMessage());
		}
	}
}
