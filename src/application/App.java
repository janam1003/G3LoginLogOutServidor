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
 * @author Dani
 */
public class App {

    private static int contador = 0;

    private static ServerSocket servidor;

    private static final Logger logger = Logger.getLogger(App.class.getName());

    private static final int PORT = (Integer.parseInt(ResourceBundle.getBundle("config.config").getString("PORT")));

    private static final int MAXUSERS = (Integer.parseInt(ResourceBundle.getBundle("config.config").getString("MAXUSERS")));

    public void iniciar() {

        try {

            logger.info("Initializing Server.");

            servidor = null;

            servidor = new ServerSocket(PORT);

            ExitThread exitThread = new ExitThread();

            exitThread.start();

            while (true) {

                Socket cliente = null;

                logger.info("Waiting for the client to connect");

                cliente = servidor.accept();

                logger.info("Client connected successfully");

                if (countThreads(1, cliente) == -1) {

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

    public synchronized static int countThreads(int x, Socket cliente) {

        if (x == 1) {

            if (contador < MAXUSERS) {

                contador++;

                WorkerThread worker = new WorkerThread(cliente);

                worker.start();

            } else {

                return -1;

            }

        } else {

            contador -= 1;

        }

        return 0;
    }
}
