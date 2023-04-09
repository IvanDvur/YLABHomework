package io.ylab.intensive.lesson05.eventsourcing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
public class MessageProducer implements PersonApi {

    @Autowired
    private MessageFactory messageFactory;
    @Autowired
    private PersonRowMapper personRowMapper;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ConnectionFactory connectionFactory;

    private ObjectMapper mapper = new ObjectMapper();
    private final String DEFAULT_QUEUE = "DB_QUEUE";
    private final String FIND_ALL_QUERY = "select * from person";
    private final String SELECT_BY_ID_QUERY = "select * from person where person_id=?";

    @Override
    public void deletePerson(Long personId) {
        try (Connection rabbitCon = connectionFactory.newConnection();
             Channel channel = rabbitCon.createChannel()) {
            channel.queueDeclare(DEFAULT_QUEUE, true, false, false, null);
            String message = mapper.writeValueAsString(messageFactory.getDeleteMessage(personId));
            channel.basicPublish("", DEFAULT_QUEUE, false, null, message.getBytes());
            System.out.println("Соообщение на удаление отправлено");
        } catch (IOException e) {
            System.out.println("Ошибка RabbitMQ");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("Превышено время ожидания");
            e.printStackTrace();
        }
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        try (Connection rabbitCon = connectionFactory.newConnection();
             Channel channel = rabbitCon.createChannel()) {
            channel.queueDeclare(DEFAULT_QUEUE, true, false, false, null);
            String message = mapper.writeValueAsString(messageFactory.getSaveMessage(personId, firstName, lastName, middleName));
            channel.basicPublish("", DEFAULT_QUEUE, false, null, message.getBytes());
            System.out.println("Соообщение на добавление отправлено");
        } catch (IOException e) {
            System.out.println("Ошибка RabbitMQ");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("Превышено время ожидания");
            e.printStackTrace();
        }
    }

    @Override
    public Person findPerson(Long personId) {
        try (java.sql.Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_QUERY)) {
            if (personId == null) {
                return null;
            }
            ps.setLong(1, personId);
            try (ResultSet rs = ps.executeQuery()) {
                Person wantedPerson = null;
                while (rs.next()) {
                    wantedPerson = new Person();
                    wantedPerson = personRowMapper.mapToObject(rs, wantedPerson);
                }
                return wantedPerson;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Person> findAll() {
        List<Person> personList = new ArrayList<>();
        try (java.sql.Connection con = dataSource.getConnection();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(FIND_ALL_QUERY)) {
            while (rs.next()) {
                Person person = new Person();
                personList.add(personRowMapper.mapToObject(rs, person));
            }
            return personList;
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении SQL-запроса");
        }
        return personList;
    }
}
