package chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Simple TCP client.
 */
public class Client implements AutoCloseable {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final Scanner scanner;

    // Multiple threads access this field, so it should be volatile.
    private volatile boolean running;

    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1",2021);
        client.run();
    }

    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.scanner = new Scanner(System.in);
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Used to make a reading|sending loop.<br/>
     * Read from stdin then send the message through the PrintWriter and kill the client if no message can be read or the message is "/leave".
     */
    public void sendMessage() {
        try(this){
            String message;
            while (running){
                if (scanner.hasNext()){
                    message = scanner.nextLine();
                    this.writer.println(message);
                }else
                    message = null;

                if (message == null || message.equals("/leave")){
                    this.kill();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Used to make a receiving loop. <br/>
     * If there is any error retrieving the message, the client will be killed.
     */
    public void receiveMessage() {
        try(this){
            while (running){
                String message = reader.readLine();
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.kill();
        }
    }

    private void kill() {
        System.out.println("--- Killing the client, goodbye mate ! ---");
        this.running = false;
    }

    /**
     * Start both the sending and receiving loops in sub-threads.
     */
    public void run() {
        this.running = true;
        new Thread(this::sendMessage).start();
        new Thread(this::receiveMessage).start();
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
        this.reader.close();
        this.socket.close();
        this.scanner.close();
    }
}
