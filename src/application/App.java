package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import thread.WorkerThread;

/**
 *
 * @author Dani
 */
public class App {
  
    private int contador = 0;
    private static final int PORT = (Integer.parseInt(ResourceBundle.getBundle("config.config").getString("PORT"))) ;
    private static final int MAXUSERS = (Integer.parseInt(ResourceBundle.getBundle("config.config").getString("MAXUSERS"))) ;

    public void iniciar() {
          while(true){
        ServerSocket servidor = null;
        Socket cliente = null;
        try {
            servidor = new ServerSocket(PORT);
            System.out.println("Waiting for the client to connect");
            cliente = servidor.accept();
            System.out.println("Client connected successfully");
            
            if(contador < MAXUSERS){
                //Añadir aqui lo que reciba el Worker
                contador++;
                WorkerThread worker = new WorkerThread() {};
                worker.run();
            } else {
            //Lo que haria el programa si se excede el numero maximo de conexiones
            }
            

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    }
    public static void main(String[] args) {
        App s1 = new App();
        s1.iniciar();
}
}
