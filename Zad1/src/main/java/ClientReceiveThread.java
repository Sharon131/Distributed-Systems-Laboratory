import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiveThread extends Thread {

    String userName;
    Socket socket;
    BufferedReader in;

    public ClientReceiveThread(Socket socket, String userName) throws IOException {
        this.userName = userName;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run() {

        while (true) {
            try {
                String received = in.readLine();

                if (!received.startsWith(userName) || (received.charAt(userName.length()) != ':')) {
                    System.out.println(received);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
