package ex1;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    private final Socket socket;
    private String message;

    public static void main(String[] args) throws IOException {
        Client client = new Client("0.0.0.0", 2021);
        client.run();
    }

    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public void read() throws IOException {
        try(InputStream in = this.socket.getInputStream()){
            message = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public void run() throws IOException {
        this.read();
        System.out.println(message);
        this.socket.close();
    }
}
