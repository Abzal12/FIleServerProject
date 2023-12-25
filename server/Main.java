package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final String DATA_DIR =
            System.getProperty("user.dir") + "/src/server/data/";

    public static void main(String[] args) {

        //System.out.println("Server started!");
        while (true) {
            try (
                    ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_ADDRESS));
                    Socket socket = serverSocket.accept();
            ) {
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                String clientMsg = input.readUTF();
                if (clientMsg.equals("exit")) {
                    return;
                }
                String[] arrClientMsg = clientMsg.split(" ");
                switch (arrClientMsg[0]) {
                    case "GET":
                        File f2 = new File(DATA_DIR + arrClientMsg[1]);
                        if (f2.exists() && !f2.isDirectory()) {
                            String fileContent = Files.readString(Paths.get(DATA_DIR + arrClientMsg[1]));
                            output.writeUTF("200 " + fileContent);
                        } else {
                            output.writeUTF("404");
                        }
                        break;

                        case "PUT":
                        File f = new File(DATA_DIR + arrClientMsg[1]);
                        if (f.exists() && !f.isDirectory()) {
                            output.writeUTF("403");
                        } else {
                            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                                    new FileOutputStream(DATA_DIR + arrClientMsg[1]), "utf-8"))) {
                                writer.write(arrClientMsg[2]);
                            }
                            output.writeUTF("200");
                        }
                        break;

                        case "DELETE":
                        File f3 = new File(DATA_DIR + arrClientMsg[1]);
                        if (f3.exists() && !f3.isDirectory()) {
                            f3.delete();
                            output.writeUTF("200");
                        } else {
                            output.writeUTF("404");
                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
