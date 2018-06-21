package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private MyServer server;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private String name;

    public ClientHandler(MyServer server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            Thread threadInMsg = new Thread(() -> {
                try {
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/auth")) {
                            String[] data = msg.split("\\s");
                            if (data.length == 3) {
                                String newNick = server.getAuthService().getNickByLoginAndPass(data[1], data[2]);
                                if (newNick != null) {
                                    if (!server.isNickBusy(newNick)) {
                                        name = newNick;
                                        sendMsg("/authok " + newNick);
                                        server.broadcastMsg("/inOrOut " + name + ": зашел в чат");
                                        server.subscribe(this);
                                        break;
                                    } else {
                                        sendMsg("/accauntuse");//"Учетная запись уже используется"
                                    }
                                } else {
                                    sendMsg("/wrongid");//"Неверный логин или пароль"
                                }
                            }
                        } else if (msg.startsWith("/newUser")) {
                            String[] data = msg.split("\\s");
                            if (data.length == 4) {
                                String answer = server.getAuthService().addNewUserInBase(data[1], data[2], data[3]);
                                if (answer != null) {
                                    if (answer.equals("loginIsBusy"))
                                        sendMsg("/loginBusy");
                                    else {
                                        sendMsg("/loginOk");
                                    }
                                } else {
                                    sendMsg("/errorLogin");
                                }
                            }
                        }
                    }
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/")) {
                            if (msg.equals("/end")) break;
                            if (msg.startsWith("/w")) {
                                String[] data = msg.split("\\s", 3);
                                server.sendPrivateMsg(this, data[1], data[2]);
                            }
                        } else
                            server.broadcastMsg(name + ": " + msg);
                    }
                } catch (IOException exp) {
                    System.out.println("соединение с " + this.getName() + " разорвано");
                } finally {
                    if (name != null) server.broadcastMsg("/inOrOut " + name + ": покинул чат");
                    name = null;
                    server.unsubscribe(this);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadInMsg.setDaemon(true);
            threadInMsg.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}