package thread;

import java.util.Scanner;
import pool.Pool;

public class ExitThread extends Thread {
	@Override
	public void run() {
		try {
			Scanner scanner = new Scanner(System.in);
			while (true) {
				int input = scanner.nextInt();
				if (input == 1) {
					Pool.closeAllConnections();
					scanner.close();
					System.exit(0);
				}
			}
		} catch (Exception e) {
			//LOGGER.severe("a");
		}
	}
}
