package server;

import java.io.*;
import java.net.Socket;

class ServerForChat extends Thread {

    // сокет, через который сервер общается с клиентом
    private final Socket socket;

    // поток чтения из сокета
    private final BufferedReader in;

    // поток записи в сокет
    private final BufferedWriter out;

    public ServerForChat(Socket socket) throws IOException {
        this.socket = socket;
        // если потоку ввода/вывода приведут к генерированию искдючения, оно проброситься дальше
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // вызываем run()
        start();
    }

    @Override
    public void run() {
        String word;
        try {
            // первое сообщение отправленное сюда - это никнейм
            word = in.readLine();
            try {
                out.write(word + "\n");
                out.flush(); // flush() нужен для выталкивания оставшихся данных
            } catch (IOException ignored) {
            }
            try {
                while (true) {
                    word = in.readLine();
//                    if (word.equals("stop")) {
//                        break;
//                    }
                    System.out.println("Echoing: " + word);
                    for (ServerForChat client : Server.serverList) {
                        client.send(word); // отослать принятое сообщение с привязанного клиента всем остальным влючая его
                    }
                }
            } catch (NullPointerException ignored) {
            }

        } catch (IOException e) {
            this.downService();
        }
    }

    private void send(String msg) {
        try {
            if (!msg.isEmpty()) {
                out.write(msg + "\n");
                out.flush();
                Thread.sleep(500);
            }

        } catch (IOException | InterruptedException ignored) {
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerForChat vr : Server.serverList) {
                    if (vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {
        }
    }
}

