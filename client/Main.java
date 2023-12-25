package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    public static void main(String[] args) {

        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            )
        {
            //System.out.println("Client started!");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
            String actionOption = scanner.nextLine();
            switch (actionOption) {
                case "1":
                    System.out.print("Enter filename: ");
                    String fileName2 = scanner.nextLine();
                    output.writeUTF("GET " + fileName2);

                    System.out.println("The request was sent.");
                    String serverResponseMsg = input.readUTF();

                    String[] arrServerResponseMsg = serverResponseMsg.split(" ");
                    switch (arrServerResponseMsg[0]) {
                        case "200":
                            System.out.println("The content of the file is: " + arrServerResponseMsg[1]);
                            break;
                        case "404":
                            System.out.println("The response says that the file was not found!");
                            break;
                    }

                    break;
                case "2":
                    System.out.print("Enter filename: ");
                    String fileName = scanner.nextLine();
                    System.out.print("Enter file content ");
                    String fileContent = scanner.nextLine();
                    output.writeUTF("PUT " + fileName + " " + fileContent);
                    System.out.println("The request was sent.");
                    String responseMsg = input.readUTF();
                    switch (responseMsg) {
                        case "200":
                            System.out.println("The response says that file was created!");
                            break;
                        case "403":
                            System.out.println("The response says that creating the file was forbidden!");
                            break;
                    }
                    break;
                case "3":
                    System.out.print("Enter filename: ");
                    String fileNameForDelete = scanner.nextLine();
                    output.writeUTF("DELETE " + fileNameForDelete);
                    System.out.println("The request was sent.");
                    String responseResult = input.readUTF();
                    switch (responseResult) {
                        case "200":
                            System.out.println("The response says that the file was successfully deleted!");
                            break;
                        case "404":
                            System.out.println("The response says that the file was not found!");
                            break;
                    }
                    break;
                case "exit":
                    output.writeUTF("exit");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
