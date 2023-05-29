package Entities;

import com.rabbitmq.client.Channel;
import connection.ConnectionManager;
import exchanges.FanoutExchange;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Audit {
    public final String queueName = "audit-" + UUID.randomUUID().toString();
    public void declareBinding() {
        try {
            Channel channel = ConnectionManager.getConnection().createChannel();
            channel.queueBind(this.queueName, FanoutExchange.fanoutExchangeName, "");
            channel.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void declareQueue() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();

        channel.queueDeclare(this.queueName, true, false, false, null);
        channel.close();
    }

    public void receiveMessage() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
            System.out.println("-- AUDIT --");
            System.out.println(new String(message.getBody()));
        }, (consumerTag) -> {
            System.out.println(consumerTag);
        });
    }

}
