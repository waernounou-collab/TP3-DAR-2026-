package serverPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    public static final int PORT = 1234;

    public static void main(String[] args) {

        AtomicInteger clientCounter = new AtomicInteger(0);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Serveur demarre sur le port " + PORT);

            while (true) {

                Socket socket = serverSocket.accept();

                int clientNumber = clientCounter.incrementAndGet();

                System.out.println(
                        "Client n°" + clientNumber
                                + " connecte depuis "
                                + socket.getRemoteSocketAddress()
                );

                ClientProcess clientProcess =
                        new ClientProcess(socket, clientNumber);

                Thread thread = new Thread(clientProcess);
                thread.start();
            }

        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}