import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class Server {

    static int portNumber = 12345;
    static ServerSocket serverSocket = null;
    static HashMap<String, LinkedList<String>> usersQueues = new HashMap<>();
    static HashMap<String, Thread> usersThreads = new HashMap<>();
    static HashMap<String, Integer> usersPorts = new HashMap<>();

    public static void createNewUserThread(String newUserName, PrintWriter out) throws IOException {
        int newUserPort = portNumber + usersPorts.size() + 1;
        // create new thread for that user
        LinkedList<String> newUserQueue = new LinkedList<>();
        Thread newUserThread = new ServerThread(newUserPort, usersQueues, newUserQueue);
        usersPorts.put(newUserName, newUserPort);
        usersQueues.put(newUserName, newUserQueue);
        usersThreads.put(newUserName, newUserThread);

        newUserThread.start();

        out.println(String.valueOf(newUserPort));
    }

    public static void checkForNewUsers() {

    }

    public static void main(String[] args) throws IOException {

        System.out.println("JAVA SERVER");

        try {
            // create socket
            serverSocket = new ServerSocket(portNumber);
//            serverSocket.setSoTimeout(1000);

            while(true){

                // accept client
                Socket clientSocket = serverSocket.accept();

                if (clientSocket != null) {
                    System.out.println("client connected");

                    // in & out streams
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    // read msg, send response
                    String newUserName = in.readLine();

                    if (!usersPorts.containsKey(newUserName)) {
                        createNewUserThread(newUserName, out);
                        System.out.println("Added new user: " + newUserName);
                    } else {
                        System.out.println("Could not add user with name: " + newUserName + ". Name is already used.");
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (serverSocket != null){
                serverSocket.close();
            }
        }
    }
}
