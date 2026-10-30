package clientPackage;

import commonPackage.Request;
import commonPackage.Response;
import serverPackage.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        try (
                Socket socket = new Socket("localhost", Server.PORT);
                Scanner scanner = new Scanner(System.in)
        ) {

            ObjectOutputStream out =
                    new ObjectOutputStream(socket.getOutputStream());

            out.flush();

            ObjectInputStream in =
                    new ObjectInputStream(socket.getInputStream());

            Response welcome = (Response) in.readObject();

            System.out.println(welcome.message);

            while (true) {

                System.out.print(
                        "Operation (+, -, *, /) ou q pour quitter : "
                );

                String operation = scanner.nextLine().trim();

                if (operation.equalsIgnoreCase("q")) {
                    break;
                }

                System.out.print("Premier nombre : ");
                double a = Double.parseDouble(scanner.nextLine());

                System.out.print("Deuxieme nombre : ");
                double b = Double.parseDouble(scanner.nextLine());

                Request request = new Request(operation, a, b);

                out.writeObject(request);
                out.flush();

                Response response = (Response) in.readObject();

                System.out.println(response.message);

                if (response.success) {
                    System.out.println("Resultat : " + response.result);
                    System.out.println(
                            "Nombre total d'operations : "
                                    + response.operationCount
                    );
                }
            }

            System.out.println("Fermeture du client.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur client : " + e.getMessage());

        } catch (NumberFormatException e) {
            System.out.println("Veuillez saisir des nombres valides.");
        }
    }
}