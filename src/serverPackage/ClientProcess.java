package serverPackage;

import commonPackage.Request;
import commonPackage.Response;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProcess implements Runnable {

    private final Socket socket;
    private final int clientNumber;

    private static int totalOperations = 0;

    public ClientProcess(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
    }

    private static synchronized int incrementOperations() {
        totalOperations++;
        System.out.println("Total des operations : " + totalOperations);
        return totalOperations;
    }

    @Override
    public void run() {

        try (Socket clientSocket = socket) {

            ObjectOutputStream out =
                    new ObjectOutputStream(clientSocket.getOutputStream());

            out.flush();

            ObjectInputStream in =
                    new ObjectInputStream(clientSocket.getInputStream());

            out.writeObject(new Response(
                    clientNumber,
                    "Connexion reussie ! Vous etes le client n°" + clientNumber,
                    0,
                    totalOperations,
                    true
            ));
            out.flush();

            while (true) {

                Request request = (Request) in.readObject();

                double result;

                switch (request.operation) {

                    case "+":
                        result = request.a + request.b;
                        break;

                    case "-":
                        result = request.a - request.b;
                        break;

                    case "*":
                        result = request.a * request.b;
                        break;

                    case "/":
                        if (request.b == 0) {
                            out.writeObject(new Response(
                                    clientNumber,
                                    "Division par zero impossible.",
                                    0,
                                    totalOperations,
                                    false
                            ));
                            out.flush();
                            continue;
                        }

                        result = request.a / request.b;
                        break;

                    default:
                        out.writeObject(new Response(
                                clientNumber,
                                "Operation inconnue.",
                                0,
                                totalOperations,
                                false
                        ));
                        out.flush();
                        continue;
                }

                int count = incrementOperations();

                out.writeObject(new Response(
                        clientNumber,
                        "Calcul effectue avec succes.",
                        result,
                        count,
                        true
                ));

                out.flush();
            }

        } catch (EOFException e) {
            System.out.println("Client n°" + clientNumber + " deconnecte.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(
                    "Erreur avec le client n°" + clientNumber
                            + " : " + e.getMessage()
            );
        }
    }
}