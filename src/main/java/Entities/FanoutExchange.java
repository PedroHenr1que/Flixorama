package Entities;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FanoutExchange {
        public static final String fanoutExchangeName = "flix-fanout-exchange";
        public static void declareExchange() throws IOException, TimeoutException {
            Channel channel = ConnectionManager.getConnection().createChannel();
            channel.exchangeDeclare(fanoutExchangeName, BuiltinExchangeType.FANOUT, true);
            channel.close();
        }
}

