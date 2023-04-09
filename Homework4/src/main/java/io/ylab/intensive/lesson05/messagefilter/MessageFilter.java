package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;


@Component
public class MessageFilter {

    private final String INPUT_QUEUE = "input";
    private final String OUTPUT_QUEUE = "output";
    private MessageParser parser;
    private Connection connection;
    private java.sql.Connection dbConnection;
    private final String FIND_WORD_QUERY = "SELECT word from slurs WHERE word ILIKE ?";

    @Autowired
    public MessageFilter(ConnectionFactory connectionFactory, DataSource dataSource, MessageParser parser) {
        try {
            this.parser = parser;
            this.connection = connectionFactory.newConnection();
            this.dbConnection = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Ошибка при соединении с базой данный");
            e.printStackTrace();
        } catch (TimeoutException | IOException e) {
            System.out.println("Ошибка соединения с RabbitMQ");
            e.printStackTrace();
        }
    }

    public void consumeMessage() {
        try (Channel channel = connection.createChannel()) {
            channel.queueDeclare(INPUT_QUEUE, true, false, false, null);
            channel.queueDeclare(OUTPUT_QUEUE, true, false, false, null);
            GetResponse inputMessage = channel.basicGet(INPUT_QUEUE, true);
            if (inputMessage == null) {
                System.out.println("Waiting for messages");
                Thread.sleep(2000);
                return;
            }
            String message = new String(inputMessage.getBody(), "UTF-8");
            String filteredMessage = filter(message);
            channel.basicPublish("", OUTPUT_QUEUE, false, null, filteredMessage.getBytes());
        } catch (IOException | TimeoutException e) {
            System.out.println("Ошибка при получении сообщения");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Прерван поток выполнения");
            e.printStackTrace();
        }
    }

    public String filter(String message) {
        List<String> words = parser.stringSplit(message);
        String result;
        try (PreparedStatement statement = dbConnection.prepareStatement(FIND_WORD_QUERY)) {
            for (int i = 0; i < words.size(); i++) {
                String currentWord = words.get(i);
                statement.setString(1, currentWord);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next() && rs.getString(1).equalsIgnoreCase(currentWord)) {
                        String censoredWord = parser.censorWord(currentWord);
                        words.set(i, censoredWord);
                    }
                }
            }
            result = String.join("",words);
            return result;
        } catch (SQLException e) {
            System.out.println("Ошибка при поиске слова");
            e.printStackTrace();
        }
        return "";
    }


}
