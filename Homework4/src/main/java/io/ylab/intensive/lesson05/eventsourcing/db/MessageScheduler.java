package io.ylab.intensive.lesson05.eventsourcing.db;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
@Component
public class MessageScheduler {

    @Autowired
    private MessageConsumer consumer;

    @Autowired
    private ConnectionFactory connectionFactory;

    public void start() {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare("DB_QUEUE", true, false, false, null);
            while (!Thread.currentThread().isInterrupted()) {
                GetResponse message = channel.basicGet("DB_QUEUE", true);
                consumer.consumeSingleMessage(message);
            }
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("TimeoutException");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
