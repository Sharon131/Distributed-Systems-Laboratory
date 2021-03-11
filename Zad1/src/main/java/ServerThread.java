import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;

public class ServerThread extends Thread {

    static String noConnectionString = "NO_CONNECTION";
    static String pingString = "PING";

    ServerSocket socket;
    String userName;
    HashMap<String, LinkedList<String>> otherQueues;
    LinkedList<String> inQueue;
    ClientAliveChecker clientAliveChecker;
    Timer clientPingTimer;
    boolean isClientAlive = true;

    public ServerThread(int portNumber, HashMap<String, LinkedList<String>> otherQueues, LinkedList<String> inQueue,
                        String userName) throws IOException {
        this.socket = new ServerSocket(portNumber);
        this.userName = userName;
        this.inQueue = inQueue;
        this.otherQueues = otherQueues;
        this.clientAliveChecker = new ClientAliveChecker(inQueue);
        this.clientPingTimer = new Timer();
    }

    public void run() {

        try {
            Socket clientSocket = socket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientPingTimer.schedule(clientAliveChecker, 5000, 5000);

            while (isClientAlive) {
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("User " + userName + " disconnected form server.");
        }
    }
}
