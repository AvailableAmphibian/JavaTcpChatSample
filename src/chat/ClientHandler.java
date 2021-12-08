package chat;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all the sub-threads and dispatches the messages correctly
 */
public class ClientHandler {
    private final List<ClientConsumer> clients;
    private static int idCounter = 0;

    public ClientHandler() {
        this.clients = new ArrayList<>();
    }

    /**
     * Used to add a new Client and start its sub thread.<br/>
     * Also notifies the other client a new one as joined.
     * @param s an accepted socket.
     * @throws IOException
     */
    public void add(Socket s) throws IOException {
        if (s == null)
            return;

        ClientConsumer clientConsumer = new ClientConsumer(s, this, idCounter++);

        Logger.logSystem("<User::" + clientConsumer.getClientId()+ "@" + s.getRemoteSocketAddress() + "> just joined the chatroom.");

        clients.forEach(consumer -> consumer.println("--- Hey ! Everyone should say hi to <User::" +clientConsumer.getClientId()+"> ! ---"));

        clientConsumer.start();
        clientConsumer.println("--- You just joined the chatroom. ---");

        clients.add(clientConsumer);
    }

    /**
     * Used to kill a Client sub-thread and remove it properly.
     * @param cc
     */
    public void remove(ClientConsumer cc){
        cc.kill();
        clients.remove(cc);
    }

    /**
     * Dispatch the message to every client.<br/>
     * Synchronized to make sure that the message isn't sent twice or something like that.
     * @param message
     */
    public synchronized void sendToAllClients(String message){
        Logger.logMessage(message);
        clients.forEach(clientConsumer -> clientConsumer.println(message));
    }
}
