package thread;

import pool.Pool;
/*
 * This class manages the thread to exit the application.
 */
public class ExitThread extends Thread {
	/*
	 * This method manages the thread to exit the application.
	 */
	@Override
	public void run() {
		try {

			while (true) {
				// The thread will wait for the user to press a key
				int input = System.in.read();
				// If the user presses '1', the server will close
				if (input == '1') {
					// Closing all the connections on the pool
					Pool.closeAllConnections();
					// Closing the server
					System.exit(0);
				}
			}
		} catch (Exception e) {
			
		}
	}
}
