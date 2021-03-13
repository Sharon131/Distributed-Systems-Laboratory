import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Timer;

public class ClientReceiveThread extends Thread {

    String userName;
    Socket socketTCP;
    DatagramSocket socketUDP;
    BufferedReader inTCP;
    PrintWriter outTCP;

    Timer pingTimer;
    ClientAliveSetter clientAliveSetter;

    public ClientReceiveThread(Socket socket, DatagramSocket socketUDP, String userName) throws IOException {
        this.userName = userName;
        this.socketTCP = socket;
        this.socketUDP = socketUDP;
        this.inTCP = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outTCP = new PrintWriter(socket.getOutputStream(), true);

        this.pingTimer = new Timer();
        this.clientAliveSetter = new ClientAliveSetter();
    }

    private void checkUDPMessage() {
        try {

        } catch (Exception e) {}
    }

    public void run() {
        pingTimer.schedule(clientAliveSetter, 0, 2000);

        while (true) {
            try {

                if (inTCP.ready()) {
                    String received = inTCP.readLine();

                    if (received.equals("DISCONNECT")) {
                        System.out.println("Connection with server is lost. Exiting.");
                        System.exit(0);
                    } else if (!received.startsWith(userName) || (received.charAt(userName.length()) != ':')) {
                        System.out.println(received);
                    }
                }


                if (!clientAliveSetter.IsPingSent()){
                    outTCP.println("PING");
                    clientAliveSetter.setPingSent();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
