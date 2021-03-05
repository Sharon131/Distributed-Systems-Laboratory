import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class JavaUdpServer {

    public static void main(String args[])
    {
        System.out.println("JAVA UDP SERVER");
        DatagramSocket socket = null;
        int portNumber = 9009;

        try{
            socket = new DatagramSocket(portNumber);
            byte[] receiveBuffer = new byte[20];

            while(true) {
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                byte[] data = receivePacket.getData(); //
                byte[] data2 = new byte[data.length-1];
                System.arraycopy(data, 1, data2, 0, data.length-1);

                String to_send;
                if (data[0] == 1) {
                    to_send = "Pong Java UDP";
                } else {
                    to_send = "Pong Python UDP";
                }

                String msg = new String(data2); //
                System.out.println("received msg: " + msg);

                byte[] sendBuffer = to_send.getBytes(); //
                
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, receivePacket.getAddress(), receivePacket.getPort());
                socket.send(sendPacket);
            }
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
