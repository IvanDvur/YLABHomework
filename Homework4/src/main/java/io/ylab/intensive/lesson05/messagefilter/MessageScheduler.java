package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class MessageScheduler {

    private MessageFilter filter;
    private DBLoader loader;
    private ConnectionFactory connectionFactory;


    @Autowired
    public MessageScheduler(MessageFilter filter, DBLoader loader, ConnectionFactory connectionFactory) {
        this.filter = filter;
        this.loader = loader;
        this.connectionFactory = connectionFactory;
    }

    public void start(File filterRules) {
        loader.fillDB(filterRules);
        try (Connection connection = connectionFactory.newConnection();
             Channel inputChannel = connection.createChannel()) {
            inputChannel.queueDeclare("input", true, false, false, null);
            while (!Thread.currentThread().isInterrupted()) {
                filter.consumeMessage();
            }
        } catch (TimeoutException | IOException e) {
            System.out.println("Ошибка при подключении RabbitMQ");
            e.printStackTrace();
        }

    }

}
