package thread;

import java.util.Scanner;
import pool.Pool;

public class ExitThread extends Thread {
	@Override
	public void run() {
		try {
			while (true) {
				int input = System.in.read();
				if (input == '1') {
					Pool.closeAllConnections();
					System.exit(0);
				}
			}
		} catch (Exception e) {
			//LOGGER.severe("a");
		}
	}
}
