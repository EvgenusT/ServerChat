package server;

import org.apache.tika.parser.txt.CharsetDetector;

import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static server.Server.*;

class ServerForChat extends Thread {

    // сокет, через который сервер общается с клиентом
    private final Socket socket;

    // поток чтения из сокета
    private final BufferedReader in;

    // поток записи в сокет
    private final BufferedWriter out;

    public ServerForChat(Socket socket) throws IOException {
        this.socket = socket;
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

                    //эксперемент с кодировкой

                    CharsetDetector detector = new CharsetDetector();
                    detector.setText(word.getBytes());
                    System.out.println("в ин пришло:  " + word + "\n" + "кодировка: " + detector.detect());

                    //перекодировка в нужный формат
                    String text = detector.getString(word.getBytes(), "KOI8");

                    System.out.println("\n" + "передано на кодировку: " + text + "\n");

                    detector.setText(text.getBytes());
                    System.out.println("Перекодировал текст:  " + text + "\n" + "кодировка: " + detector.detect());

                    //организация отключения клиента по нажанию кнопки: "Отключиться от чата"
                    Matcher matcher = Pattern.compile("[:]\\t.+$").matcher(word);
                    String result;
                    for (result = ""; matcher.find(); result = matcher.group()) {
                    }
                    String resOut = result.substring(2);

                    if (resOut.equals("StoP+-+")) {
                        downService();
                    } else {
                        System.out.println("Echoing: " + word);
                        for (ServerForChat client : clientList) {

                            client.send(word); // отослать принятое сообщение с привязанного клиента всем остальным влючая его
                        }
                    }
                }
            } catch (StringIndexOutOfBoundsException d) {
                this.downService();
            }

        } catch (IOException e) {
            this.downService();
        }
    }

    private void send(String msg) throws UnsupportedEncodingException {

//        String utf8 = new String(msg.getBytes("Windows-1251"), "UTF-8");
        String utf8 = new String(msg.getBytes("UTF-8"), "Windows-1251");

        try {
            if (!utf8.isEmpty()) {
                out.write(utf8 + "\n");
                out.flush();
                Thread.sleep(400);
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
                for (ServerForChat vr : clientList) {
                    if (vr.equals(this)) {
                        vr.interrupt();
                        clientList.remove(this);
                        System.out.println("клиент покинул чат");
                    }
                }

            }
        } catch (IOException ignored) {
        }
    }
}

