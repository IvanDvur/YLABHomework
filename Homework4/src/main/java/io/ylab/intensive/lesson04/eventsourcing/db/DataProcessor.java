package io.ylab.intensive.lesson04.eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import io.ylab.intensive.lesson04.eventsourcing.Messages.DeleteMessage;
import io.ylab.intensive.lesson04.eventsourcing.Messages.Message;
import io.ylab.intensive.lesson04.eventsourcing.Messages.SaveMessage;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class DataProcessor {
    private DataSource dataSource;
    private ConnectionFactory rabbitmqConnectionFactory;
    private ObjectMapper mapper = new ObjectMapper();


    public DataProcessor(DataSource dataSource, ConnectionFactory rabbitmqConnectionFactory) {
        this.dataSource = dataSource;
        this.rabbitmqConnectionFactory = rabbitmqConnectionFactory;
    }

    public void consumeMessages() {
        try(Connection connection = rabbitmqConnectionFactory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.queueDeclare("DB_QUEUE", true, false, false, null);
            while (!Thread.currentThread().isInterrupted()) {
                GetResponse message = channel.basicGet("DB_QUEUE", true);
                if (message == null) {
                    System.out.println("Waiting for messages...");
                    Thread.sleep(2000L);
                } else {
                    String json = new String(message.getBody(), "UTF-8");
                    Message consumedMessage;
                    if (json.contains("insert")) {
                        consumedMessage = mapper.readValue(json, SaveMessage.class);
                        insertIntoDB((SaveMessage) consumedMessage);
                    }
                    if (json.contains("delete")) {
                        consumedMessage = mapper.readValue(json, DeleteMessage.class);
                        deleteFromDB((DeleteMessage) consumedMessage);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("TimeoutException");
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void deleteFromDB(DeleteMessage deleteMessage) {
        String deleteQuery = "delete from person where person_id = ?";
        String findIfExistsQuery = "select * from person where person_id = ?";
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteQuery);
             PreparedStatement psFind = connection.prepareStatement(findIfExistsQuery)) {
            psFind.setLong(1, deleteMessage.getId());
            ResultSet rs = psFind.executeQuery();
            if (rs.next()) {
                ps.setLong(1, deleteMessage.getId());
                ps.execute();
                rs.close();
                System.out.println("Запись успешно удалена");
                return;
            }
            System.out.println("Удаление не выполнено, т.к по заданному id не найдено не одного значения");
            rs.close();
        } catch (SQLException e) {
            System.out.println("Ошибка при обработке запроса");
            e.printStackTrace();
        }
    }

    private void insertIntoDB(SaveMessage saveMessage) {
        String insertQuery = "insert into person(person_id, first_name, last_name, middle_name) values(?,?,?,?)";
        String updateQuery = "update person set first_name=?,last_name=?,middle_name=? where person_id=?";
        String findIfExistsQuery = "select * from person where person_id = ?";
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement psInsert = connection.prepareStatement(insertQuery);
             PreparedStatement psUpdate = connection.prepareStatement(updateQuery);
             PreparedStatement psFind = connection.prepareStatement(findIfExistsQuery)) {
            psFind.setLong(1, saveMessage.getId());
            ResultSet rs = psFind.executeQuery();
            if (!rs.next()) {
                psInsert.setLong(1, saveMessage.getId());
                psInsert.setString(2, saveMessage.getFirstName());
                psInsert.setString(3, saveMessage.getLastName());
                psInsert.setString(4, saveMessage.getMiddleName());
                psInsert.execute();
                System.out.println("Объект успешно добавлен");
                return;
            }
            psUpdate.setString(1, saveMessage.getFirstName());
            psUpdate.setString(2, saveMessage.getLastName());
            psUpdate.setString(3, saveMessage.getMiddleName());
            psUpdate.setLong(4, saveMessage.getId());
            psUpdate.execute();
            System.out.println("Объект успешно обновлён");
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении SQL-запроса");
            e.printStackTrace();
        }
    }
}
