import com.rabbitmq.client.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Deliverer {

    public static void main(String[] argv) throws Exception {

        // info
        System.out.println("Z2 Deliverer");

        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // exchange
//        String EXCHANGE_NAME = "exchange1";
//        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter products you want to handle: ");
        System.out.println("When you finish, enter 'end'");

        // queue & bind
        LinkedList<String> queuesNames = new LinkedList<>();
        String product = br.readLine();

        while (!product.equals("end")) {
            queuesNames.add(product);
            channel.queueDeclare(product, false, false, false, null).getQueue();

            product = br.readLine();
        }

        // start listening
        System.out.println("Configuration is done. Waiting for orders...");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received: " + message);

                String teamName = properties.getReplyTo();
                channel.basicPublish("", teamName, null, message.getBytes("UTF-8"));
            }
        };

        for (String queueName: queuesNames) {
            channel.basicConsume(queueName, true, consumer);
        }

        System.out.println("Waiting for messages...");
    }
}
