package server;

import Log.MainLogs;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    // порт для подключения
    public static final int PORT = 18080;

    // количество подключений
    public static final int CLIENT_NUNBER = 10;
    static int countClient = 0;

    public static ServerSocket server = null;
    public static Socket socet = null;

    // список клиентов
    public static LinkedList<ServerForChat> clientList = new LinkedList<>();

    static {
        try {
            // создаем серверный сокет на порту с указанием максимального количества подключений
            server = new ServerSocket(PORT, CLIENT_NUNBER, InetAddress.getByName("0.0.0.0"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startServer() throws IOException {
        System.out.println("Сервер запущен");
        MainLogs.logs();
        try {
            while (true) {
                // Блокируется до возникновения нового соединения:
                socet = server.accept();

                try {
                    // добавить новое соединенние в список
                    ServerForChat client = new ServerForChat(socet);
                    clientList.add(client);
                    // каждое подключение клиента обрабатываем в новом потоке
                    new Thread(client).start();

                    System.out.println("Подключен новый пользователь");
                    System.out.println("Всего пользователей в чате: " + clientList.size());

                } catch (IOException e) {
                    socet.close();
                }
            }
        } finally {
            try {
                socet.close();
                socet.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}