package exchanges;

import connection.ConnectionManager;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class HeadersExchange {
    public static final String headerExchangeName = "flix-exchange";
    public static void declareExchange() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.exchangeDeclare(headerExchangeName, BuiltinExchangeType.HEADERS, true);
        channel.close();
    }


}
