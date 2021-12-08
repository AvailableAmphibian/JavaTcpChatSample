package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * Used to manage a client and its sub-thread.
 */
public class ClientConsumer extends Thread implements AutoCloseable {
    private final Socket socket;
    private final ClientHandler handler;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final int clientId;

    private volatile boolean running;

    public ClientConsumer(Socket socket, ClientHandler handler, int id) throws IOException {
        this.socket = socket;
        this.handler = handler;
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientId = id;
    }

    @Override
    public void run() {
        this.running = true;
        try (this) {
            while (running) {
                final String received = reader.readLine();
                final LocalDateTime now = LocalDateTime.now();

                if (received == null || received.equals("/leave")){
                    handler.remove(this);
                    handler.sendToAllClients("--- <User:" + clientId + "> has just disconnected. ---");
                }else
                    handler.sendToAllClients(clientId + "|" + now + " ~\n" + received);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void kill(){
        running = false;
    }

    public void println(String message) {
        writer.println(message);
    }

    @Override
    public void close() throws Exception {
        this.writer.close();
        this.reader.close();
        this.socket.close();
    }

    public int getClientId() {
        return clientId;
    }
}
