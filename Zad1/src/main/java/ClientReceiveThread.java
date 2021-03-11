import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;

public class ClientReceiveThread extends Thread {

    String userName;
    Socket socket;
    BufferedReader in;
    PrintWriter out;

    Timer pingTimer;
    ClientAliveSetter clientAliveSetter;

    public ClientReceiveThread(Socket socket, String userName) throws IOException {
        this.userName = userName;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);

        this.pingTimer = new Timer();
        this.clientAliveSetter = new ClientAliveSetter();
    }

    public void run() {
        pingTimer.schedule(clientAliveSetter, 0, 2000);

        while (true) {
            try {

                if (in.ready()) {
                    String received = in.readLine();

                    if (received.equals("DISCONNECT")) {
                        System.out.println("Connection with server is lost. Exiting.");
                        System.exit(0);
                    } else if (!received.startsWith(userName) || (received.charAt(userName.length()) != ':')) {
                        System.out.println(received);
                    }
                }


                if (!clientAliveSetter.IsPingSent()){
                    out.println("PING");
                    clientAliveSetter.setPingSent();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
