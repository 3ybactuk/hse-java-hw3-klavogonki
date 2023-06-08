package ru.hse.javaprogramming;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DefaultServer implements Server {
    private static final int THREADS = 10;
    private static final int MAX_PLAYERS_PER_GAME = 3;
    private final int port;
    private final ServerSocket serverSocket;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
    private final List<Player> playersInGame = new ArrayList<>();

    public DefaultServer(int port) throws IOException {
        this.port = port;

        serverSocket = new ServerSocket(port);
    }

    @Override
    public void start() {
        Logging.printTimestampMessage("Server is started");
        Acceptor acceptor = new Acceptor();
        executorService.execute(acceptor);
    }

    private class Acceptor implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Handler handler = new Handler(socket);
                    executorService.execute(handler);
                } catch (SocketException e) {
                    Logging.printTimestampMessage("ServerSocket closed: " + e.getMessage());
                    break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class Handler implements Runnable {
        private final Socket socket;
        private Player player;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (socket;
                 DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                 DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
                Logging.printTimestampMessage("Client " + socket.getInetAddress().getHostAddress() + " connected to the server");

                boolean shouldExit = false;

                while (!shouldExit) {
                    String[] request = dataInputStream.readUTF().split(" ", 2);
                    if (request.length < 1) {
                        Logging.printTimestampMessage("Invalid request from client " + socket.getInetAddress().getHostAddress());
                        dataOutputStream.writeInt(HTTPStatusCodes.BAD_REQUEST.getCode());
                        dataOutputStream.flush();
                    }
                    Logging.printTimestampMessage("Got request: [" + request[0] + "] from client: " + socket.getInetAddress().getHostAddress());

                    switch (request[0]) {
                        case "GET_TEXT" -> {
                            String text = FileLib.getTextFromFile("amogus.txt");

                            dataOutputStream.writeInt(HTTPStatusCodes.OK.getCode());
                            dataOutputStream.writeUTF(text);
                            dataOutputStream.flush();

                            Logging.printTimestampMessage("Sent text to the client " + socket.getInetAddress().getHostAddress());
                        }
                        case "GET_PLAYERS" -> {

                        }
                        case "SEND_CLIENT_NAME" -> {
                            if (request.length < 2) {
                                Logging.printTimestampMessage("Client didn't send name: " + socket.getInetAddress().getHostAddress());

                                dataOutputStream.writeInt(HTTPStatusCodes.BAD_REQUEST.getCode());
                            } else {
                                String name = request[1];
                                Logging.printTimestampMessage("Server got name " + name + " from client: " + socket.getInetAddress().getHostAddress());

                                player = new Player(name, socket.getInetAddress().getHostAddress());

                                dataOutputStream.writeInt(HTTPStatusCodes.OK.getCode());
                            }
                            dataOutputStream.flush();
                        }
                        default -> Logging.printTimestampMessage("Invalid request from client " + socket.getInetAddress().getHostAddress());
                    }
                }
            } catch (IOException e) {
                Logging.printTimestampMessage(e + " Lost connection to client " + socket.getInetAddress().getHostAddress());
            } finally {
                removePlayerFromGame(player);
            }

            Logging.printTimestampMessage("Client disconnected: " + socket.getInetAddress().getHostAddress());
        }

        private synchronized List<Player> getPlayersForGame() {
            return new ArrayList<>(playersInGame);
        }

        private synchronized boolean isPlayerNameAvailable(String name) {
            return playersInGame.stream().noneMatch(player -> player.getName().equals(name));
        }

        private synchronized void addPlayerToGame(Player player) {
            playersInGame.add(player);
        }

        private synchronized void removePlayerFromGame(Player playerToRemove) {
            playersInGame.removeIf(player -> player.getName().equals(playerToRemove.getName()));
        }
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
                throw new IllegalStateException("Couldn't await termination");
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
