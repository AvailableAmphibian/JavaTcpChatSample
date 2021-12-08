package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * Simple TCP Server.
 */
public class Server {
    private final ServerSocket serverSocket;
    private final ClientHandler clientHandler;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clientHandler = new ClientHandler();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(2021);
        server.run();
    }

    /**
     * Used to start the server. It will also log when it connects and the basic logs needed.
     * @throws IOException if the socket couldn't have been accepted.
     */
    public void run() throws IOException {
        Logger.logServer("Starting server...");
        Logger.logServer("Server interactions will always be displayed using `===` before and after the message; Seen only in logs.");
        Logger.logServer("System messages will always be displayed using `---` before and after the message.");

        try(this.serverSocket){
            Logger.logServer("Server started at " + LocalDateTime.now() + " on " + serverSocket.getLocalSocketAddress() + ", period.");
            Logger.logServer("Now accepting sockets.");
            while (true){
                Socket s = serverSocket.accept();
                clientHandler.add(s);
            }
        }
    }
}
