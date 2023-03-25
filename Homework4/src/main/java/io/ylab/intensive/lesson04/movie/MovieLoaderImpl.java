package io.ylab.intensive.lesson04.movie;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class MovieLoaderImpl implements MovieLoader {
    private DataSource dataSource;

    public MovieLoaderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void loadData(File file) {
        // РЕАЛИЗАЦИЮ ПИШЕМ ТУТ
        String insertQuery = "insert into movie (year, length, title, subject, actors, actress, director," +
                "popularity ,awards) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Movie> movieList = parseCsvToList(file);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (Movie m : movieList) {
                checkAndPrepare(preparedStatement,m);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Movie> parseCsvToList(File file) {
        if (file == null) {
            return null;
        }
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            //Пропуск двух строк заголовка
            bufferedReader.readLine();
            bufferedReader.readLine();
            String line;
            List<Movie> movies = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                movies.add(parseLine(line));
            }
            return movies;
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла");
            e.printStackTrace();
        }
        return null;
    }

    private Movie parseLine(String s) {
        try {
            String[] sm;
            sm = s.split(";");
            if (sm.length != 10) {
                throw new IOException();
            }
            return new Movie(
                    sm[0].isEmpty() ? -1 : Integer.parseInt(sm[0]),
                    sm[1].isEmpty() ? -1 : Integer.parseInt(sm[1]),
                    sm[2], sm[3], sm[4],sm[5], sm[6],
                    sm[7].isEmpty()?-1:Integer.parseInt(sm[7]),
                    parseBoolean(sm[8]));
        } catch (IOException e) {
            System.out.println("Ошибка формата файла");
            e.printStackTrace();
        }
        return null;
    }

    private Boolean parseBoolean(String s) {
        if (s.isEmpty()) {
            return null;
        }
        return s.equals("Yes");
    }

    private void checkAndPrepare(PreparedStatement ps, Movie m) throws SQLException {
        for (int i = 1, j = 0; i <= 9; i++, j++) {
            Object o = m.getParameters().get(j);
            if (o instanceof String && ((String) o).isEmpty()) {
                ps.setNull(i, Types.VARCHAR);
                continue;
            }
            if (o instanceof String && !((String) o).isEmpty()) {
                ps.setString(i, (String)o);
                continue;
            }
            if (o instanceof Integer && (Integer) o == -1) {
                ps.setNull(i, Types.INTEGER);
                continue;
            }
            if (o instanceof Integer && (Integer) o != -1) {
                ps.setInt(i, (Integer) o);
                continue;
            }
            if (o instanceof Boolean && o == null) {
                ps.setNull(i, Types.BOOLEAN);
                continue;
            }
            if (o instanceof Boolean && o != null) {
                ps.setBoolean(i, (Boolean) o);
            }
        }
    }
}
