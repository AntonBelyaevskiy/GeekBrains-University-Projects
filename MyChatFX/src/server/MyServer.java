package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MyServer {
    private static final int PORT = 8189;

    private ServerSocket serverSocket;
    private Socket socket;
    private Vector<ClientHandler> clients;
    private AuthService authService;

    public MyServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            authService = new AuthService();
            authService.connect();
            clients = new Vector<>();
            while (true) {
                System.out.println("Waiting for client connection...");
                socket = serverSocket.accept();
                System.out.println("Client is connected. "
                        + socket.getInetAddress() + " " + socket.getPort() + " " + socket.getLocalPort());
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server Connection Error");
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        authService.disconnect();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler client : clients)
            if (client.getName().equals(nick)) return true;
        return false;
    }

    public synchronized void broadcastMsg(String msg) {
        for (ClientHandler client : clients)
            client.sendMsg(msg);
    }

    public synchronized void sendPrivateMsg(ClientHandler from, String toClient, String msg) {
        for (ClientHandler client : clients) {
            if (client.getName().equals(toClient)) {
                client.sendMsg("личное от " + from.getName() + ": " + msg);
                from.sendMsg("отправлено " + toClient + ": " + msg);
                return;
            }
        }
        from.sendMsg("клиент " + toClient + " не найден");
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public void broadcastClientList() {
        StringBuilder sb = new StringBuilder("/clientslist ");
        for (ClientHandler ch : clients) {
            sb.append(ch.getName()).append(" ");
        }
        String out = sb.toString();
        for (ClientHandler ch : clients) {
            ch.sendMsg(out);
        }
    }
}