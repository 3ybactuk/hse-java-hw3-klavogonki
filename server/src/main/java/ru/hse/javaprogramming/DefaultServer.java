package ru.hse.javaprogramming;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DefaultServer implements Server {
    private static final int THREADS = 10;
    private static final int MAX_PLAYERS_PER_GAME = 3;
    private final int port;
    private final ServerSocket serverSocket;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
    private final List<Player> playersList = new ArrayList<>();
    private final List<Game> games = new ArrayList<>();

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
        private final Timer updateTimer;
        private Game game;

        public Handler(Socket socket) {
            this.socket = socket;
            this.updateTimer = new Timer();
        }

        @Override
        public void run() {
            try (socket;
                 DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                 DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
                Logging.printTimestampMessage("Client " + socket.getInetAddress().getHostAddress() + " connected to the server");

                boolean shouldExit = false;

                while (!shouldExit) {
                    String[] request = dataInputStream.readUTF().split(" ", 2);
                    if (request.length < 1) {
                        Logging.printTimestampMessage("Invalid request from client " + socket.getInetAddress().getHostAddress());
                        continue;
                    }
                    Logging.printTimestampMessage("Got request: [" + request[0] + "] from client: " + socket.getInetAddress().getHostAddress());

                    switch (request[0]) {
                        case "SEND_CLIENT_NAME" -> {
                            if (request.length < 2) {
                                Logging.printTimestampMessage("Client didn't send name: " + socket.getInetAddress().getHostAddress());
                            } else {
                                String name = request[1];
                                Logging.printTimestampMessage("Server got name " + name + " from client: " + socket.getInetAddress().getHostAddress());

                                if (isPlayerNameAvailable(name)) {
                                    player = new Player(name, socket.getInetAddress().getHostAddress());
                                    addPlayerToGame(player);
                                    dataOutputStream.writeInt(HTTPStatusCodes.OK.getCode());
                                    dataOutputStream.writeUTF(game.getText());
                                    dataOutputStream.flush();

                                    updateTimer.scheduleAtFixedRate(new TimerTask() {
                                        @Override
                                        public void run() {
                                            try {
                                                // Send update message to the client
                                                if (player != null) {
//                                                    dataOutputStream.writeUTF("TIME_LEFT");
                                                    dataOutputStream.writeInt(game.secondsRemaining);
//                                                    dataOutputStream.writeUTF("IS_TEXT_HIDDEN");
                                                    dataOutputStream.writeBoolean(game.isTextHidden);
//                                                    dataOutputStream.writeUTF("IS_GAME_STARTED");
                                                    dataOutputStream.writeBoolean(game.isGameStarted);
//                                                    dataOutputStream.writeUTF("IS_GAME_ENDED");
                                                    dataOutputStream.writeBoolean(game.isGameEnded);
//                                                    dataOutputStream.writeUTF("PLAYER_INFO");
                                                    dataOutputStream.writeInt(game.countPlayers());
                                                    dataOutputStream.flush();

                                                    for (Player player : game.getPlayers()) {
                                                        objectOutputStream.writeObject(player.getPlayerInfoMap());
                                                        objectOutputStream.flush();
                                                    }
                                                }
                                            } catch (IOException e) {
                                                removePlayerFromGame(player);
                                                Logging.printTimestampMessage("Stopping server updates for client " + socket.getInetAddress().getHostAddress());
                                                cancel();
                                            }
                                        }
                                    }, 0, 1_000);
                                } else {
                                    Logging.printTimestampMessage("Can't connect player " + name + " from: " + socket.getInetAddress().getHostAddress() + " " +
                                            "because the name is occupied.");
                                    dataOutputStream.writeInt(HTTPStatusCodes.FORBIDDEN.getCode());
                                    dataOutputStream.flush();
                                    shouldExit = true;
                                }
                            }
                        }
                        case "PLAYER_UPDATE" -> {
                            Map<String, String> playerUpdate = (Map<String, String>) objectInputStream.readObject();
                            Logging.printTimestampMessage(playerUpdate.toString());
                        }
                        case "GET_TEXT" -> {
                            dataOutputStream.writeUTF(game.getText());
                            dataOutputStream.flush();
                        }
                        default -> Logging.printTimestampMessage("Invalid request from client " + socket.getInetAddress().getHostAddress());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                Logging.printTimestampMessage(e + " Lost connection to client " + socket.getInetAddress().getHostAddress());
            } finally {
                removePlayerFromGame(player);
            }

            Logging.printTimestampMessage("Client disconnected: " + socket.getInetAddress().getHostAddress());
        }

        private synchronized List<Player> getPlayersForGame() {
            return new ArrayList<>(playersList);
        }

        private synchronized boolean isPlayerNameAvailable(String name) {
            return playersList.stream().noneMatch(player -> player.getName().equals(name));
        }

        private synchronized void addPlayerToGame(Player player) {
            playersList.add(player);
            boolean isGameFound = false;
            for (Game game : games) {
                if (game.isTextHidden && game.countPlayers() < game.MAX_PLAYERS) {
                    isGameFound = true;
                    game.addPlayer(player);
                    player.setGame(game);
                    this.game = game;
                }
            }

            if (!isGameFound) {
                this.game = new Game(FileLib.getRandomText());
                this.game.addPlayer(player);
                player.setGame(this.game);
                games.add(this.game);

            }
        }

        private synchronized void removePlayerFromGame(Player playerToRemove) {
            if (playerToRemove != null) {
                playersList.removeIf(player -> player.getName().equals(playerToRemove.getName()));
            }
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
