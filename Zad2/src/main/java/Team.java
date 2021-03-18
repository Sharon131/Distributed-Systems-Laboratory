import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Team {

    public static void main(String argv[]) throws Exception {

        // info
        System.out.println("Z2 Team");

        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // exchange
        String EXCHANGE_NAME = "exchange1";
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // queue for messages from admin
        String queueNameAdmin = channel.queueDeclare().getQueue();
        channel.queueBind(queueNameAdmin, EXCHANGE_NAME, "teams");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter your team's name: ");
        String teamName = br.readLine();

        channel.queueDeclare(teamName, false, false, false, null).getQueue();

        System.out.println("Configuration finished.");
        // read msg
        System.out.println("Enter products you want to order: ");
        System.out.println("When you finish, enter 'end'");

        String product = br.readLine();

        while (!product.equals("end")) {
//            if (br.ready()) {
            channel.queueDeclare(product, false, false, false, null);

            AMQP.BasicProperties prop = new AMQP.BasicProperties.Builder().replyTo(teamName).build();
            channel.basicPublish("", product, prop, product.getBytes("UTF-8"));
            channel.basicPublish("", "admin", prop, product.getBytes("UTF-8"));

            product = br.readLine();
//            }
        }

        // publish
        System.out.println("Order sent. Waiting for acknowledgement.");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received: " + message);
            }
        };

        Consumer adminMessageConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Message from admin: " + message);
            }
        };

        channel.basicConsume(teamName, true, consumer);
        channel.basicConsume(queueNameAdmin, true, adminMessageConsumer);

        String line = br.readLine();
        while (!line.equals("exit")) {
            line = br.readLine();
        }

        System.exit(0);
    }
}
