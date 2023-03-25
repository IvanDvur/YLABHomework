package io.ylab.intensive.lesson04.persistentmap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Класс, методы которого надо реализовать
 */
public class PersistentMapImpl implements PersistentMap {

    private static String instanceName;
    private DataSource dataSource;

    public PersistentMapImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void init(String name) {
        instanceName = name;
    }

    @Override
    public boolean containsKey(String key) {
        String containsQuery = "select key from persistent_map where map_name=? and key=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(containsQuery)) {
            ps.setString(1, instanceName);
            ps.setString(2, key);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return false;
            }
            String result = rs.getString("key");
            if (result.equals(key)) {
                return true;
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Oшибка");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> getKeys() {
        String selectKeysQuery = "select key from persistent_map where map_name=?";
        List<String> keyList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectKeysQuery)) {
            ps.setString(1, instanceName);
            ResultSet keys = ps.executeQuery();

            while (keys.next()) {
                keyList.add(keys.getString("key"));
            }
            keys.close();
            return keyList;
        } catch (SQLException e) {
            System.out.println("Ошибка обработки запроса");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String get(String key) {
        String selectValueQuery = "select value from persistent_map WHERE map_name=? and key=?";
        String value;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectValueQuery)) {

            ps.setString(1, instanceName);
            ps.setString(2, key);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }
            value = rs.getString("value");
            rs.close();
            return value;
        } catch (SQLException e) {
            System.out.println("Ошибка запроса");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(String key) {
        String removeQuery = "delete from persistent_map where map_name=? and key = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(removeQuery)) {
            if (get(key) == null) {
                System.out.println("Указанного ключа не существует в текущем экземпляре");
                return;
            }
            ps.setString(1, instanceName);
            ps.setString(2, key);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(String key, String value) throws SQLException {
        String insertQuery = "insert into persistent_map(map_name, KEY, value) values (?,?,?)";
        String deleteKeyIfPresentQuery = "delete from persistent_map where map_name = ? and key=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement psInsert = connection.prepareStatement(insertQuery);
             PreparedStatement psDelete = connection.prepareStatement(deleteKeyIfPresentQuery)) {

            psInsert.setString(1, instanceName);
            psInsert.setString(2, key);
            psInsert.setString(3, value);
            if (get(key) != null) {
                psDelete.setString(1, instanceName);
                psDelete.setString(2, key);
                psDelete.execute();
                psInsert.execute();
                return;
            }
            psInsert.execute();
        }
    }

    @Override
    public void clear() {
        String clearQuery = "delete from persistent_map where map_name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(clearQuery)) {
            ps.setString(1, instanceName);
            ps.execute();
        } catch (SQLException e) {
            System.out.println("Ошибка обработки запроса");
            e.printStackTrace();
        }
    }
}
