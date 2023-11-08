package thread;

import java.util.logging.Logger;
import pool.Pool;

public class ExitThread extends Thread {

    private static final Logger logger = Logger.getLogger(ExitThread.class.getName());

    @Override
    public void run() {

        try {

            while (true) {

                logger.info("WRITE '1' TO CLOSE THE SERVER.\n");

                int input = System.in.read();

                if (input == '1') {

                    logger.info("Received exit signal. Closing all connections and exiting.");

                    Pool.closeAllConnections();

                    System.exit(0);

                }

            }

        } catch (Exception e) {

            logger.severe("Error: " + e.getMessage());

        }
    }
}
