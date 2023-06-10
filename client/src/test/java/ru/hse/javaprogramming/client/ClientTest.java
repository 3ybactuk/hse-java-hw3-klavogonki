package ru.hse.javaprogramming.client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientTest {
    private Client client;
    private ByteArrayInputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private Socket socket;
    private PlayController playController;

    @BeforeEach
    public void setup() throws IOException {
        outputStream = new ByteArrayOutputStream();
        socket = new Socket() {
            @Override
            public InputStream getInputStream() throws IOException {
                return inputStream;
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                return outputStream;
            }
        };
        playController = new PlayController();

        client = new Client("localhost", 8080, "John", playController);
        client.setSocket(socket);
    }

    @Test
    public void testConnectToServer_SuccessfulConnection() throws IOException {
        String serverResponse = "200\nHello, world!";
        inputStream = new ByteArrayInputStream(serverResponse.getBytes());

        boolean result = client.connectToServer();

        Assertions.assertTrue(result);
        Assertions.assertEquals("SEND_CLIENT_NAME John\n", outputStream.toString());
    }

    @Test
    public void testConnectToServer_FailedConnection() throws IOException {
        String serverResponse = "500\n";
        inputStream = new ByteArrayInputStream(serverResponse.getBytes());

        boolean result = client.connectToServer();

        Assertions.assertFalse(result);
        Assertions.assertEquals("SEND_CLIENT_NAME John\n", outputStream.toString());
    }

    @Test
    public void testSendPlayerUpdate() throws IOException, ClassNotFoundException {
        Map<String, String> playerInfo = new HashMap<>();
        playerInfo.put("name", "John");
        playerInfo.put("wrong_symbols", "10");
        playerInfo.put("total_symbols", "100");

        client.sendPlayerUpdate();

        Assertions.assertEquals("PLAYER_UPDATE\n", outputStream.toString());

        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        Map<String, String> sentPlayerInfo = (Map<String, String>) objectInputStream.readObject();

        Assertions.assertEquals(playerInfo, sentPlayerInfo);
    }

    @Test
    public void testUpdateStats() {
        Map<String, String> playerInfoMap = new HashMap<>();
        playerInfoMap.put("name", "John");
        playerInfoMap.put("text_length", "200");
        playerInfoMap.put("total_symbols", "100");
        playerInfoMap.put("wrong_symbols", "5");
        playerInfoMap.put("is_you", "true");
        playerInfoMap.put("connection_interrupted", "false");

        client.updateStats(playerInfoMap);

        PlayerStat playerStat = client.getPlayerStatList().get(0);

        Assertions.assertNotNull(playerStat);
        Assertions.assertEquals("John", playerStat.getName());
        Assertions.assertEquals(100, playerStat.totalSymbols);
        Assertions.assertEquals(5, playerStat.wrongSymbols);
        Assertions.assertTrue(playerStat.isYou);
        Assertions.assertFalse(playerStat.isConnectionInterrupted);
    }

    @Test
    public void testDisconnectFromServer() throws IOException {
        client.disconnectFromServer();

        Assertions.assertTrue(outputStream.toString().contains("close"));
    }
}
