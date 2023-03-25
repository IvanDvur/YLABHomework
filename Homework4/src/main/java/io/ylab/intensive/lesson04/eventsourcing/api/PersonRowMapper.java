package io.ylab.intensive.lesson04.eventsourcing.api;

import io.ylab.intensive.lesson04.eventsourcing.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper{


    public static Person mapToObject(ResultSet rs,Person person){

        try {
            person.setId(rs.getLong("person_id"));
            person.setName(rs.getString("first_name"));
            person.setLastName(rs.getString("last_name"));
            person.setMiddleName(rs.getString("middle_name"));
            return person;
        } catch (SQLException e) {
            System.out.println("Ошибка");
        }
        return null;
    }
}
