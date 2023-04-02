package io.ylab.intensive.lesson05.eventsourcing.db;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.GetResponse;
import io.ylab.intensive.lesson04.eventsourcing.Messages.DeleteMessage;
import io.ylab.intensive.lesson04.eventsourcing.Messages.Message;
import io.ylab.intensive.lesson04.eventsourcing.Messages.SaveMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MessageConsumer {

    @Autowired
    private DataSource dataSource;
    private ObjectMapper mapper;
    private final String INSERT_QUERY = "insert into person(person_id, first_name, last_name, middle_name) values(?,?,?,?)";
    private final String UPDATE_QUERY = "update person set first_name=?,last_name=?,middle_name=? where person_id=?";
    private final String FIND_QUERY = "select * from person where person_id = ?";
    private final String DELETE_QUERY = "delete from person where person_id = ?";

    @Autowired
    public MessageConsumer() {
        this.mapper = new ObjectMapper();
    }

    public void consumeSingleMessage(GetResponse message) throws InterruptedException, IOException {
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

    private void deleteFromDB(DeleteMessage deleteMessage) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_QUERY);
             PreparedStatement psFind = connection.prepareStatement(FIND_QUERY)) {
            if (deleteMessage.getId() == null) {
                return;
            }
            psFind.setLong(1, deleteMessage.getId());
            try (ResultSet rs = psFind.executeQuery()) {
                if (rs.next()) {
                    ps.setLong(1, deleteMessage.getId());
                    ps.execute();
                    System.out.println("Запись успешно удалена");
                    return;
                }
            }
            System.out.println("Удаление не выполнено, т.к по заданному id не найдено не одного значения");
        } catch (SQLException e) {
            System.out.println("Ошибка при обработке SQL-запроса");
            e.printStackTrace();
        }
    }

    private void insertIntoDB(SaveMessage saveMessage) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement psInsert = connection.prepareStatement(INSERT_QUERY);
             PreparedStatement psUpdate = connection.prepareStatement(UPDATE_QUERY);
             PreparedStatement psFind = connection.prepareStatement(FIND_QUERY)) {
            if (saveMessage.getId() == null) {
                return;
            }
            psFind.setLong(1, saveMessage.getId());
            try (ResultSet rs = psFind.executeQuery()) {
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
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при обработке SQL-запроса");
            e.printStackTrace();
        }
    }

}
