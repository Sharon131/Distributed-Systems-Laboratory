import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;

public class ServerThread extends Thread {

    static String noConnectionString = "NO_CONNECTION";
    static String pingString = "PING";

    int portNumber;
    ServerSocket serverSocket;
    String userName;
    HashMap<String, LinkedList<String>> otherQueues;
    HashMap<String, Integer> otherPorts;
    LinkedList<String> inQueue;
    ClientAliveChecker clientAliveChecker;
    Timer clientPingTimer;
    boolean isClientAlive = true;

    public ServerThread(int portNumber, HashMap<String, LinkedList<String>> otherQueues,
                        LinkedList<String> inQueue, HashMap<String, Integer> usersPorts, String userName) throws IOException {
        this.portNumber = portNumber;
        this.serverSocket = new ServerSocket(portNumber);
        this.userName = userName;
        this.inQueue = inQueue;
        this.otherQueues = otherQueues;
        this.otherPorts = usersPorts;
        this.clientAliveChecker = new ClientAliveChecker(inQueue);
        this.clientPingTimer = new Timer();
    }

    private void checkForMessageFromDesignatedClient(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // check for message from user
        if (in.ready()) {
            String received = in.readLine();

            if (received.equals(pingString)) {
                clientAliveChecker.setToTrue();
            } else {
                for (LinkedList<String> queue : otherQueues.values()) {
                    queue.addFirst(received);
                }
            }
        }
    }

    private void checkForMessagesFromOthers(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        // check for messages from others
        if (!inQueue.isEmpty()) {
            String message = inQueue.removeLast();

            if (message.equals(noConnectionString)) {
                isClientAlive = false;
            } else {
                out.println(message);
            }
        }
    }

    public void run() {
        Socket clientSocket = null;


        try {
            clientSocket = serverSocket.accept();

            clientPingTimer.schedule(clientAliveChecker, 5000, 5000);

            while (isClientAlive) {
                checkForMessageFromDesignatedClient(clientSocket);
                checkForMessagesFromOthers(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null){
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("User " + userName + " disconnected fromm server.");
        }
    }
}
