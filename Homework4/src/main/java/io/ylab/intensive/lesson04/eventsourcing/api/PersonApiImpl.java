package io.ylab.intensive.lesson04.eventsourcing.api;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.eventsourcing.Messages.DeleteMessage;
import io.ylab.intensive.lesson04.eventsourcing.Messages.SaveMessage;
import io.ylab.intensive.lesson04.eventsourcing.Person;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {


    private ConnectionFactory connectionFactory;
    private ObjectMapper mapper = new ObjectMapper();
    private final String DEFAULT_QUEUE = "DB_QUEUE";

    public PersonApiImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void deletePerson(Long personId) {
        try (Connection connection = connectionFactory.newConnection();
             Channel apiToRabbitChannel = connection.createChannel()) {
            apiToRabbitChannel.queueDeclare(DEFAULT_QUEUE, true, false, false, null);
            String message = mapper.writeValueAsString(new DeleteMessage("delete", personId));
            apiToRabbitChannel.basicPublish("", DEFAULT_QUEUE, false, null, message.getBytes());
            System.out.println("Соообщение на удаление отправлено");
        } catch (IOException e) {
            System.out.println("Ошибка чтения-записи");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("Превышено время ожидания");
            e.printStackTrace();
        }
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        try (Connection connection = connectionFactory.newConnection();
            Channel apiToRabbitChannel = connection.createChannel()){
            apiToRabbitChannel.queueDeclare(DEFAULT_QUEUE, true, false, false, null);
            String message = mapper.writeValueAsString(new SaveMessage("insert", personId, firstName, lastName, middleName));
            apiToRabbitChannel.basicPublish("", DEFAULT_QUEUE, false, null, message.getBytes());
            System.out.println("Соообщение на добавление отправлено");
        } catch (IOException e) {
            System.out.println("Ошибка чтения записи");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("Превышено время ожидания");
            e.printStackTrace();
        }
    }

    @Override
    public Person findPerson(Long personId) {
        String selectByIdQuery = "select * from person where person_id=?";
        int size = 0;
        try (java.sql.Connection connection = DbUtil.buildDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(selectByIdQuery)) {

            ps.setLong(1, personId);
            ResultSet set = ps.executeQuery();
            Person wantedPerson = new Person();
            while (set.next()) {
                wantedPerson = PersonRowMapper.mapToObject(set, wantedPerson);
                size++;
            }
            if (size != 1) {
                return null;
            }
            set.close();
            return wantedPerson;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Person> findAll() {
        List<Person> personList = new ArrayList<>();
        String findAllQuery = "select * from person";
        try (java.sql.Connection connection = DbUtil.buildDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(findAllQuery)) {
            while (rs.next()) {
                Person person = new Person();
                personList.add(PersonRowMapper.mapToObject(rs, person));
            }
            if (personList.size() == 0) {
                return null;
            }
            return personList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
