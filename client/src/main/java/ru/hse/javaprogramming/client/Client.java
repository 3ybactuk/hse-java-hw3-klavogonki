package ru.hse.javaprogramming.client;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;
    private final Socket socket;
    private final String name;
    private int wrongSymbols = 0;
    private int totalSymbols = 0;
    private String text;
//    private final Set<PlayerStat> playerStats = new TreeSet<>();
    private final Map<String, PlayerStat> playerStats = new HashMap<>();
    private final PlayController playController;
    private int timerSeconds;
    public boolean isTextHidden;
    public boolean isGameStarted;
    public boolean isGameEnded;

    public Client(String serverIp, int serverPort, String name, PlayController playController) throws IOException {
        this.playController = playController;
        this.socket = new Socket(serverIp, serverPort);
        this.name = name;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public boolean connectToServer() {
        try {
            dataOutputStream.writeUTF("SEND_CLIENT_NAME " + name);
            dataOutputStream.flush();

            int serverReturnCode = dataInputStream.readInt();
            if (serverReturnCode != 200) {
                disconnectFromServer();
                return false;
            }

            text = dataInputStream.readUTF();

            Thread messageListener = new Thread(new MessageListener());
            messageListener.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void sendPlayerUpdate() {
        try {
            dataOutputStream.writeUTF("PLAYER_UPDATE");
            dataOutputStream.flush();
            Map<String, String> playerInfo = new HashMap<>();
            playerInfo.put("name", name);
            playerInfo.put("wrong_symbols", Integer.toString(wrongSymbols));
            playerInfo.put("total_symbols", Integer.toString(totalSymbols));

            objectOutputStream.writeObject(playerInfo);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateStats(Map<String, String> playerInfoMap) {
        PlayerStat tmpPlayer = new PlayerStat(playerInfoMap.get("name"), Integer.parseInt(playerInfoMap.get("text_length")));
//        playerStats.add(tmpPlayer);
        playerStats.put(tmpPlayer.getName(), tmpPlayer);

    }

    public String getPlayerStats() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        List<PlayerStat> list = new ArrayList<>(playerStats.values());
        Collections.sort(list);
        for (PlayerStat playerStat : list) {
            stringBuilder.append(i++).append(". ").append(playerStat.toString()).append("\n");
        }

        return stringBuilder.toString();
    }

    public String getText() {
        return text;
    }

    public int getTimerSeconds() {
        return timerSeconds;
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

            if (objectInputStream != null) {
                objectInputStream.close();
            }

            if (objectOutputStream != null) {
                objectOutputStream.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MessageListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    timerSeconds = dataInputStream.readInt();
                    isTextHidden = dataInputStream.readBoolean();
                    isGameStarted = dataInputStream.readBoolean();
                    isGameEnded = dataInputStream.readBoolean();

                    int size = dataInputStream.readInt();

                    for (int i = 0; i < size; i++) {
                        Map<String, String> serverMessage = (Map<String, String>) objectInputStream.readObject();
//                        System.out.println(serverMessage.toString());
                        updateStats(serverMessage);
                    }

//                    playController.setPlayerStats(getPlayerStats());
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Can't listen to messages - client disconnected.");
            }
        }
    }
}
