import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class ServerThread extends Thread {
    ServerSocket socket;
    HashMap<String, LinkedList<String>> otherQueues;
    LinkedList<String> inQueue;

    public ServerThread(int portNumber, HashMap<String, LinkedList<String>> otherQueues, LinkedList<String> inQueue) throws IOException {
        this.socket = new ServerSocket(portNumber);
        this.inQueue = inQueue;
        this.otherQueues = otherQueues;
    }

    public void run() {

        try {
            Socket clientSocket = socket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                // check for message from user
                if (in.ready()) {
                    String received = in.readLine();
                    for (LinkedList<String> queue: otherQueues.values()) {
                        queue.addFirst(received);
                    }
                }

                // check for messages from others
                if (!inQueue.isEmpty()) {
                    String to_send = inQueue.removeLast();
                    out.println(to_send);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
