import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class JavaUdpClient {

    public static void main(String args[]) throws Exception
    {
        System.out.println("JAVA UDP CLIENT");
        DatagramSocket socket = null;
        int portNumber = 9009;

        try {
            socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            byte[] sendBuffer = "Ping Java Udp".getBytes();
            byte[] sendBuffer2 = new byte[sendBuffer.length+1];     //
            System.arraycopy(sendBuffer, 0, sendBuffer2, 1, sendBuffer.length);
            sendBuffer2[0] = 1;

            DatagramPacket sendPacket = new DatagramPacket(sendBuffer2, sendBuffer2.length, address, portNumber);
            socket.send(sendPacket);
            
            byte[] receiveBuffer = new byte[20];  //
            Arrays.fill(receiveBuffer, (byte)0);
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);

            String msg = new String(receivePacket.getData());
            System.out.println("received msg: " + msg);     //

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
