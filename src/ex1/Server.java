package ex1;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Server {
    public static void main(String[] args) throws IOException {
        Server server = new Server(2021);
        server.run();
    }

    private final ServerSocket serverSocket;


    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void run() throws IOException {
        System.out.println(this.serverSocket.getLocalSocketAddress());

        final byte[] message = "Welcome on my beautiful server... And goodbye !\n".getBytes(StandardCharsets.UTF_8);
        try(this.serverSocket){
            while(true){
                final Socket socket = serverSocket.accept();
                final OutputStream out = socket.getOutputStream();

                final String ip = socket.getLocalSocketAddress().toString();
                final LocalDateTime now = LocalDateTime.now();
                System.out.println("Socket from " + ip +" accepted at :" + now);

                out.write(message);

                out.close();
                socket.close();
            }
        }
    }
}
