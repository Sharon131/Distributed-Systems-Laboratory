import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class ServerThreadUDP extends Thread{

    DatagramSocket socketUDP;
    byte[] receiveBuffer = new byte[20];

    public ServerThreadUDP(DatagramSocket socketUDP) {
        this.socketUDP = socketUDP;
    }

    public void run() {

        while(true) {
            try {
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                socketUDP.receive(receivePacket);

                // send to other clients

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
