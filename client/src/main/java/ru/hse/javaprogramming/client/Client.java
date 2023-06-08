package ru.hse.javaprogramming.client;

import java.io.*;
import java.net.Socket;

public class Client {
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final Socket socket;
    private final String name;

    public Client(String serverIp, int serverPort, String name) throws IOException {
        this.socket = new Socket(serverIp, serverPort);
        this.name = name;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void connectToServer() {
        try {
            dataOutputStream.writeUTF("SEND_CLIENT_NAME " + name);
            dataOutputStream.flush();


            int serverReturnCode = dataInputStream.readInt();

            if (serverReturnCode != 200) {
                System.out.println("Couldn't send client name: " + serverReturnCode);
                throw new RuntimeException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getText() {
        try {
            dataOutputStream.writeUTF("GET_TEXT");
            dataOutputStream.flush();

            int serverReturnCode = dataInputStream.readInt();
            if (serverReturnCode != 200) {
                System.out.println("Couldn't get text: " + serverReturnCode);
            } else {
                return dataInputStream.readUTF();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public void disconnectFromServer() {
        System.out.println("Disconnecting...");
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }

            if (dataOutputStream != null) {
                dataOutputStream.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
