import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class JavaClient {

    public static void main(String[] args) throws IOException {
        System.out.println("JAVA TCP Client");

        String hostName = "localhost";
        int portNumber = 12345;
        Socket socket = null;

        try {
            // create scanner
            Scanner sc= new Scanner(System.in);

            // create socket
            socket = new Socket(hostName, portNumber);

            // in & out streams
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//            String response = in.readLine();

            while (true) {
                System.out.print("Enter a message to send:\r\n");
                String to_send= sc.nextLine();
                System.out.print("You have entered: "+to_send);

                //send to server
                out.println(to_send);
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
