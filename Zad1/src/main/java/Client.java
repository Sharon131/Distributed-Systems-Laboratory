import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Client {

    static String hostName = "localhost";
    static int portNumber = 12345;
    static Socket socket = null;
    static DatagramSocket socketUDP = null;
    static PrintWriter out;
    static BufferedReader in;

    static Scanner sc = null;
    static String userName = null;

    static ClientReceiveThread receiveThread;


    public static void connectToServer() throws IOException {
        out.println(userName);
        int newPortNumber = parseInt(in.readLine());

        if (newPortNumber > 0) {
            socket = new Socket(hostName, newPortNumber);
            socketUDP = new DatagramSocket(newPortNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } else {
            socket = null;
        }
    }

    public static void handleUDPMessage() throws IOException {
        System.out.println("Write your message to send over UDP.");
        System.out.println("When it is done, write character 'U' again to end.");

        InetAddress address = InetAddress.getByName("localhost");

        String line = sc.nextLine();

        while (!line.equals("U")) {

            byte[] sendBuffer = line.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, portNumber);
            socketUDP.send(sendPacket);

            line = sc.nextLine();
        }

    }

    public static void messageLoop(PrintWriter out) {
        while (true) {
            try {
                String to_send= sc.nextLine();
                //send to server

                if (to_send.equals("U")) {
                    handleUDPMessage();
                } else {
                    out.println(userName + ": " + to_send);
                }
            } catch (Exception e) {}
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("JAVA TCP Client");

        try {
            // create scanner and socket
            sc= new Scanner(System.in);
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Enter your user name:");
            userName = sc.nextLine();

            System.out.println("Hello " + userName + "!");

            System.out.println("Connecting to server...");
            connectToServer();

            if (socket != null) {
                receiveThread = new ClientReceiveThread(socket, socketUDP, userName);
                receiveThread.start();
                System.out.println("Connection established. Now you can write something to other users:");

                messageLoop(out);
            } else {
                System.out.println("Connection failed. Try different user name.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null){
                socket.close();
            }
        }

    }
}
