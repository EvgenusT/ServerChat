package server;

import java.io.IOException;
import java.nio.charset.Charset;

public class Main {

    public static void main(String[] args) throws IOException {

        String defaultCharset = Charset.defaultCharset().toString();
        System.out.println(defaultCharset);

        Server server = new Server();
        server.startServer();
    }
}
