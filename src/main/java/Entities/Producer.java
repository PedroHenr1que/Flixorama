package Entities;

import Enums.CountryEnum;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    public CountryEnum country;

    public void setCountry(CountryEnum country) {
        this.country = country;
    }

    public CountryEnum getCountry() {
        return country;
    }

    public void sendMessage(HeadersMessage message) throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();

        System.out.println("Sending message...");
        System.out.println(message.getHeaders());
        BasicProperties properties = new BasicProperties
                .Builder().headers(message.getHeaders()).build();

        channel.basicPublish(FanoutExchange.fanoutExchangeName, "", properties, message.getFormattedMessage().getBytes());
        channel.close();
    }
}
