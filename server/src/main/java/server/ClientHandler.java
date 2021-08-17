package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    Socket socket;
    Server server;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    private boolean authenticated;
    private String nickname;
    private String login;

    public ClientHandler(Socket socket, Server server) {

        try {
            this.socket = socket;
            this.server = server;

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    //цикл аутентификации
                    while (true) {
                        String str = inputStream.readUTF();
                            if (str.equals("/end")) {
                                sendMessage("/end");
                                System.out.println("Клиент отключился");
                                break;
                            }
                            if (str.startsWith("/auth ")) {
                                String[] token = str.split("\\s");
                                nickname = server
                                        .getAuthService()
                                        .getNicknameByLoginAndPassword(token[1], token[2]);
                                login = token[1];
                                if (nickname != null) {
                                    if (!server.isLoginAuthorised(login)) {
                                        sendMessage("/authok " + nickname);
                                        server.subscribe(this);
                                        authenticated = true;
                                        break;
                                    } else {
                                        sendMessage("С этим логином уже вошли");
                                    }
                                } else {
                                    sendMessage("Неверный логин/пароль");
                                }
                            }
                            if (str.startsWith("/reg ")) {
                                String[] token = str.split("\\s");
                                if (token.length < 4) {
                                    continue;
                                }

                                boolean regOk = server.getAuthService()
                                        .registration(token[1],token[2],token[3]);
                                if (regOk) {
                                    sendMessage("/regYes");
                                } else {
                                    sendMessage("/regNo");
                                }
                            }
                    }
                    while (authenticated) {
                        String str = inputStream.readUTF();

                        if (str.startsWith("/")) {
                            if (str.equals("/end")) {
                                sendMessage("/end");
                                System.out.println("Клиент отключился");
                                break;
                            }
                            if (str.startsWith("/w")) {
                                String[] token = str.split("\\s+",3);
                                if (token.length < 3 ) {
                                    continue;
                                }
                                server.privateMessage(this, token[1],token[2]);
                            }
                        } else {
                            server.broadcastMessage(this, str);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.unsubscribe(this);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }
}
