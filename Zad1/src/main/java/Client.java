import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Client {

    static String hostName = "localhost";
    static int portNumber = 12345;
    static Socket socket = null;
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
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } else {
            socket = null;
        }
    }

    public static void messageLoop(PrintWriter out) {
        while (true) {
            try {
                String to_send= sc.nextLine();
                //send to server
                out.println(userName + ": " + to_send);
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
                receiveThread = new ClientReceiveThread(socket, userName);
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
