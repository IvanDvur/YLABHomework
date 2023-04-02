package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

@Component
public class DBLoader {

    private Connection connection;
    private final String CREATE_TABLE_QUERY = "create table if not exists slurs (word varchar)";
    private final String INSERT_DATA_QUERY = "insert into slurs(word) values(?)";
    private final String CLEAR_TABLE_QUERY = "delete from slurs";

    @Autowired
    public DBLoader(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Ошибка при заполнении базы данных");
        }
    }

    public void fillDB(File file) {
        try (PreparedStatement createTableStatement = connection.prepareStatement(CREATE_TABLE_QUERY);
             PreparedStatement loadDataStatement = connection.prepareStatement(INSERT_DATA_QUERY);
             PreparedStatement clearTableStatement = connection.prepareStatement(CLEAR_TABLE_QUERY)) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet rs = metaData.getTables(null, null, "slurs", new String[]{"TABLE"})) {
                if (!rs.next()) {
                    createTableStatement.execute();
                }
            }
            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                clearTableStatement.execute();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    loadDataStatement.setString(1, line);
                    loadDataStatement.execute();
                }
            } catch (IOException e) {
                System.out.println("Ошибка при чтении файла");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении SQL-запроса");
        }
    }
}
