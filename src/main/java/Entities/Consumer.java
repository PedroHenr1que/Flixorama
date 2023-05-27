package Entities;

import Enums.CountryEnum;
import Enums.GenderEnum;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public CountryEnum country;
    public HashMap<String, Object> queueHeaders = new HashMap<String, Object>();
    public String queueName = UUID.randomUUID().toString();
    public String consumerName = "";

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String name) {
        this.consumerName = name;
        this.queueName = name + "-" + this.queueName;
    }
    public CountryEnum getCountry() {
        return country;
    }

    public void setCountry(CountryEnum country) {
        this.country = country;
    }

    public HashMap<String, Object> getQueueHeaders() {
        return this.queueHeaders;
    }

    public void setQueueHeaders(List<GenderEnum> genders) {
        queueHeaders.put("x-match", "any");
        for (GenderEnum gender: genders) {
            queueHeaders.put(gender.toString(), this.country.toString());
        }
    }

    public void declareBinding() {
        try {
            Channel channel = ConnectionManager.getConnection().createChannel();
            channel.queueBind(this.queueName, HeadersExchange.headerExchangeName, "", this.getQueueHeaders());
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
            System.out.printf("----- CONSUMER %s -----\n", this.consumerName);
            System.out.println(new String(message.getBody()));
        }, (consumerTag) -> {
            System.out.println(consumerTag);
        });
    }
}
