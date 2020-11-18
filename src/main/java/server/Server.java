package server;

import javafx.application.Platform;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    // порт для подключения
    public static final int PORT = 18080;
    // список клиентов
    public static LinkedList<ServerForChat> serverList = new LinkedList<>();
    // количество подключений
    public static final int CLIENT_NUNBER = 10;

    public static ServerSocket server = null;

    static {
        try {
            // создаем серверный сокет с на порту с указанием максимального количества подключений
            server = new ServerSocket(PORT, CLIENT_NUNBER, InetAddress.getByName("0.0.0.0"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startServer() throws IOException {


        System.out.println("Сервер запущен");
        try {
            while (true) {
                // Блокируется до возникновения нового соединения:
                Socket socket = null;
                socket = server.accept();

                try {
                    // добавить новое соединенние в список
                    serverList.add(new ServerForChat(socket));
                    System.out.println("Подключен новый пользователь");
                } catch (IOException e) {
                    socket.close();

                }
            }

        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clouseServer() throws IOException {
        server.close();
        System.out.println("Сервер остановлен");
    }
}