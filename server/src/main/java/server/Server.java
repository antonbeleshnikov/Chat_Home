package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;

    private List<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public Server() {
        clients = new CopyOnWriteArrayList<>();
        authService = new SimpleAuthService();
        try {
            server = new ServerSocket(PORT);
            System.out.println("Сервер запущен");


            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMessage(ClientHandler sender, String message) {
        String format_message = String.format("[ %s ]: %s", sender.getNickname(), message);
        for (ClientHandler c : clients) {
            c.sendMessage(format_message);
        }
    }

    public void privateMessage(ClientHandler sender, String receiver, String message) {
        String format_message = String.format("[ %s ] to [ %s ]: %s", sender.getNickname(), receiver, message);
        for (ClientHandler c : clients) {
            if (c.getNickname().equals(receiver)) {
                c.sendMessage(format_message);
                if (!c.equals(sender)) {
                    sender.sendMessage(format_message);
                }
                return;
            }
        }
        sender.sendMessage("Not found user: " + receiver);
    }

    public boolean isLoginAuthorised(String login) {
        for (ClientHandler c : clients) {
            if (c.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public void broadcastClientList() {
        StringBuilder sb = new StringBuilder("/clientList");
        for (ClientHandler c : clients) {
            sb.append(" ").append(c.getNickname());
        }

        String message = sb.toString();
        for (ClientHandler c : clients) {
            c.sendMessage(message);
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }
}
