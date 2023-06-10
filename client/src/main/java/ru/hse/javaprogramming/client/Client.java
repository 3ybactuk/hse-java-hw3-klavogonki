package ru.hse.javaprogramming.client;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * The Client class represents a client that connects to a server and interacts with it.
 */
public class Client {
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;
    private Socket socket;
    private final String name;
    public int wrongSymbols = 0;
    public int totalSymbols = 0;
    public int charPos = 0;
    private String text;
    private final Map<String, PlayerStat> playerStats = new HashMap<>();
    private final PlayController playController;
    private int timerSeconds;
    public int timeElapsed;
    public boolean isTextHidden;
    public boolean isGameStarted;
    public boolean isGameEnded;

    /**
     * Creates a new instance of the Client class.
     *
     * @param serverIp       the IP address of the server to connect to
     * @param serverPort     the port of the server to connect to
     * @param name           the name of the client
     * @param playController the PlayController instance associated with the client
     * @throws IOException if an I/O error occurs when creating the client
     */
    public Client(String serverIp, int serverPort, String name, PlayController playController) throws IOException {
        this.playController = playController;
        this.socket = new Socket(serverIp, serverPort);
        this.name = name;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Sets the socket of the client.
     *
     * @param socket1 the socket to set
     */
    public void setSocket(Socket socket1) {
        this.socket = socket1;
    }

    /**
     * Connects the client to the server.
     *
     * @return true if the connection is successful, false otherwise
     */
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

    /**
     * Sends an update of the player's information to the server.
     */
    public void sendPlayerUpdate() {
        try {
            dataOutputStream.writeUTF("PLAYER_UPDATE");
            dataOutputStream.flush();
            Map<String, String> playerInfo = new HashMap<>();
            playerInfo.put("name", name);
            playerInfo.put("wrong_symbols", Integer.toString(wrongSymbols));
            playerInfo.put("total_symbols", Integer.toString(charPos));

            objectOutputStream.writeObject(playerInfo);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the player statistics based on the received player information map.
     *
     * @param playerInfoMap the map containing the player information
     */
    public void updateStats(Map<String, String> playerInfoMap) {
        System.out.println(playerInfoMap);
        PlayerStat tmpPlayer = new PlayerStat(playerInfoMap.get("name"), Integer.parseInt(playerInfoMap.get("text_length")));
        tmpPlayer.totalSymbols = Integer.parseInt(playerInfoMap.get("total_symbols"));
        tmpPlayer.wrongSymbols = Integer.parseInt(playerInfoMap.get("wrong_symbols"));
        tmpPlayer.isYou = Boolean.parseBoolean(playerInfoMap.get("is_you"));
        tmpPlayer.isConnectionInterrupted = Boolean.parseBoolean(playerInfoMap.get("connection_interrupted"));
        tmpPlayer.secondsElapsed = timeElapsed;

        playerStats.put(tmpPlayer.getName(), tmpPlayer);
    }

    /**
     * Returns the player statistics as a formatted string.
     *
     * @return the player statistics string
     */
    public String getPlayerStatsString() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        List<PlayerStat> list = new ArrayList<>(playerStats.values());
        Collections.sort(list);

        for (PlayerStat playerStat : list) {
            stringBuilder.append(i++).append(". ").append(playerStat.toString()).append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Returns the player statistics as a list of PlayerStat objects.
     *
     * @return the player statistics as a list
     */
    public List<PlayerStat> getPlayerStatList() {
        return (List<PlayerStat>) playerStats.values();
    }

    /**
     * Returns the text received from the server.
     *
     * @return the received text
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the timer duration in seconds.
     *
     * @return the timer duration
     */
    public int getTimerSeconds() {
        return timerSeconds;
    }

    /**
     * Disconnects the client from the server.
     */
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

    /**
     * Sets the text to be used in the game.
     *
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }


    /**
     * A runnable class that listens to messages from the server.
     */
    private class MessageListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    timeElapsed = dataInputStream.readInt();
                    System.out.println(timeElapsed);
                    timerSeconds = dataInputStream.readInt();
                    isTextHidden = dataInputStream.readBoolean();
                    isGameStarted = dataInputStream.readBoolean();
                    isGameEnded = dataInputStream.readBoolean();

                    int size = dataInputStream.readInt();

                    for (int i = 0; i < size; i++) {
                        Map<String, String> serverMessage = (Map<String, String>) objectInputStream.readObject();
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
