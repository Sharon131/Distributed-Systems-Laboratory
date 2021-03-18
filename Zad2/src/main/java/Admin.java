import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Admin {

    public static void main(String[] argv) throws Exception {
        // info
        System.out.println("Z2 Admin");

        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String EXCHANGE_NAME = "exchange1";
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // declare in queue
        String QUEUE_NAME = "admin";
        channel.queueDeclare(QUEUE_NAME, false, true, false, null).getQueue();

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received message from " + properties.getReplyTo() + ": " + message);
            }
        };

        channel.basicConsume(QUEUE_NAME, true, consumer);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Configuration done. Now you can enter message you want to send.");
        System.out.println("If you want to send your message to clients, write 'teams:' before you message.");
        System.out.println("If you want to write to deliveres, write 'deliverers'.");
        System.out.println("If you want to write to all, write 'all'. Example: 'all: Go home.'");

        while (true) {
            String line = br.readLine();
            String[] data = line.split(":");

            if (data[0].equals("all")) {
                channel.basicPublish(EXCHANGE_NAME, "teams", null, data[1].getBytes("UTF-8"));
                channel.basicPublish(EXCHANGE_NAME, "deliverers", null, data[1].getBytes("UTF-8"));
            } else if (data[0].equals("teams") || data[0].equals("deliverers")) {
                channel.basicPublish(EXCHANGE_NAME, data[0], null, data[1].getBytes("UTF-8"));
            } else {
                System.out.println("Not known receiver. Try again.");
            }
        }
    }
}
