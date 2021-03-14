import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Server {

    static int portNumber = 12345;
    static ServerSocket serverSocket = null;
    static DatagramSocket socketUDP = null;
    static HashMap<String, LinkedList<String>> usersQueues = new HashMap<>();
    static HashMap<String, Thread> usersThreads = new HashMap<>();
    static HashMap<String, Integer> usersPorts = new HashMap<>();

    public static Socket tryToAcceptANewClient() {
        Socket clientSocket = null;
        try {
            serverSocket.setSoTimeout(100);
            clientSocket = serverSocket.accept();
        } catch (SocketTimeoutException e) {}
        catch (Exception e) {}
        finally {
            return clientSocket;
        }
    }

    public static void createNewUserThread(String newUserName, PrintWriter out) throws IOException {
        int newUserPort = portNumber + usersPorts.size() + 1;
        // create new thread for that user
        LinkedList<String> newUserQueue = new LinkedList<>();
        Thread newUserThread = new ServerThread(newUserPort, usersQueues, newUserQueue, usersPorts, newUserName);
        usersPorts.put(newUserName, newUserPort);
        usersQueues.put(newUserName, newUserQueue);
        usersThreads.put(newUserName, newUserThread);

        newUserThread.start();

        out.println(String.valueOf(newUserPort));
    }

    public static void checkForUDPMessages() {
        try {
            socketUDP.setSoTimeout(100);
            byte[] receiveBuffer = new byte[1000];
            InetAddress address = InetAddress.getByName("localhost");
            Arrays.fill(receiveBuffer, (byte)0);
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            socketUDP.receive(receivePacket);

            for (String name: usersPorts.keySet()) {
                if (!(new String(receiveBuffer, StandardCharsets.UTF_8).startsWith(name))) {
                    DatagramPacket sendPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length, address, usersPorts.get(name));
                    socketUDP.send(sendPacket);
                }
            }

        } catch (Exception e) {}
    }

    public static void checkUsersThreads() {
        for (String name: usersThreads.keySet()) {
            if (!usersThreads.get(name).isAlive()) {
                usersThreads.remove(name);
                usersPorts.remove(name);
                usersQueues.remove(name);
            }
        }
    }

    public static void main(String[] args) throws IOException {

        Runtime.getRuntime().addShutdownHook(new ServerCleanUpThread(usersQueues, usersThreads));
        System.out.println("JAVA SERVER");

        try {
            // create socket
            serverSocket = new ServerSocket(portNumber);
            socketUDP = new DatagramSocket(portNumber);

            while(true){

                // accept client
                Socket clientSocket = tryToAcceptANewClient();

                if (clientSocket != null) {
                    System.out.println("Client connected");

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

                checkForUDPMessages();

                checkUsersThreads();
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
