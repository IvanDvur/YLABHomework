package io.ylab.intensive.lesson05.sqlquerybuilder;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostgreSQLQueryBuilder implements SQLQueryBuilder {

    private DatabaseMetaData metaData;
    private Connection connection;
    private final String[] TABLES_CAT = new String[]{"TABLE", "SYSTEM TABLE"};

    @Autowired
    public PostgreSQLQueryBuilder(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
            this.metaData = connection.getMetaData();
        } catch (SQLException e) {
            System.out.println("Ошибка подключения");
            e.printStackTrace();
        }
    }

    @Override
    public String queryForTable(String tableName) {
        String resultQuery = null;
        List<String> columnList = new ArrayList<>();
        if (getTables().contains(tableName)) {
            try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
                while (columns.next()) {
                    columnList.add(columns.getString(4));
                }
                resultQuery = "SELECT " + String.join(", ", columnList) + " FROM " + tableName;
                return resultQuery;
            } catch (SQLException e) {
                System.out.println("Ошибка при составлении SQL-запроса");
                e.printStackTrace();
            }
        }
        return resultQuery;
    }

    @Override
    public List<String> getTables() {
        List<String> tables = new ArrayList<>();
        try (ResultSet schemaRs = metaData.getSchemas()) {
            while (schemaRs.next()) {
                String currentSchema = schemaRs.getString(1);
                try (ResultSet tableRS = metaData.getTables(null, currentSchema, "%", TABLES_CAT)) {
                    while (tableRS.next()) {
                        tables.add(tableRS.getString("TABLE_NAME"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Exception");
            e.printStackTrace();
        }
        return tables;
    }

    @PreDestroy
    private void closeConnection() {
        try {
            this.connection.close();
            System.out.println("Соединение закрыто");
        } catch (SQLException e) {
            System.out.println("Ошибка при закрытии соединения");
        }
    }
}

