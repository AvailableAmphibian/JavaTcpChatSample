package chat;

/**
 * Class used as a way to know what happened with our chat.
 */
public class Logger {
    /**
     * Used to log a server related message.
     * @param message the message logged.
     */
    public static void logServer(String message){
        System.out.println("=== " + message + "===");
    }

    /**
     * Used to log a message related to the application.
     * @param message the message logged.
     */
    public static void logSystem(String message){
        System.out.println("--- " + message + " ---");
    }

    /**
     * Used to log a message sent by a user.
     * @param message the message logged.
     */
    public static void logMessage(String message){
        System.out.println(message);
    }
}
