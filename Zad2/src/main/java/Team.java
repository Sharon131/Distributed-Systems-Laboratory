import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Team {

    public static void waitForAcknowledgement(Channel channel, String queueName, int numberToReceive) throws IOException {
        for (int i=0;i<numberToReceive;i++) {
            GetResponse response = channel.basicGet(queueName, true);
            if (response != null) {
                String message = new String(response.getBody(), "UTF-8");
                System.out.println("Received: " + message);
            }
        }
    }


    public static void main(String argv[]) throws Exception {

        // info
        System.out.println("Z2 Team");

        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // exchange
//        String EXCHANGE_NAME = "exchange1";
//        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter your team's name: ");
        String teamName = br.readLine();

        channel.queueDeclare(teamName, false, false, false, null).getQueue();

        System.out.println("Configuration finished.");
        // read msg
        System.out.println("Enter products you want to order: ");
        System.out.println("When you finish, enter 'end'");

        String product = br.readLine();
        int numberOfOrders = 0;

        while (!product.equals("end")) {
            channel.queueDeclare(product, false, false, false, null);

            AMQP.BasicProperties prop = new AMQP.BasicProperties.Builder().replyTo(teamName).build();
            channel.basicPublish("", product, prop, product.getBytes("UTF-8"));
            numberOfOrders++;
            product = br.readLine();
        }

        // publish
        System.out.println("Order sent. Waiting for acknowledgement.");

        waitForAcknowledgement(channel, teamName, numberOfOrders);
    }
}
