package server;

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

    static int countClient = 0;

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
        try {
            while (true) {
                // Блокируется до возникновения нового соединения:
                Socket socket = null;
                socket = server.accept();

                try {
                    // добавить новое соединенние в список
                    ServerForChat client = new ServerForChat(socket);
                    serverList.add(client);
                    System.out.println(serverList);
                    countClient++;
                    // каждое подключение клиента обрабатываем в новом потоке
//                    new Thread(client).start();
                    System.out.println("Подключен новый пользователь");
                    System.out.println("Всего пользователей в чате: " + countClient);

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