package io.ylab.intensive.lesson04.filesort;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;
import javax.sql.DataSource;

public class FileSortImpl implements FileSorter {
    private DataSource dataSource;
    private final String SQLE_MESSAGE = "Ошибка при выполнении SQL-запроса";
    private final String IOE_MESSAGE = "Ошибка ввода-вывода";
    private int batchSize;
    private int nbOfLines;
    private int nbOfBatches;

    public FileSortImpl(DataSource dataSource, int batchSize) {
        this.dataSource = dataSource;
        this.batchSize = batchSize;
    }

    @Override
    public File sort(File data) {
        try (Connection connection = dataSource.getConnection()) {
            double startNoBatch = System.currentTimeMillis()*0.001;
            writeToDBWithoutBatchProcessing(data,connection);
            double endNoBatch = System.currentTimeMillis()*0.001;
            System.out.println("Время добавки без batch-processing: "+ (endNoBatch-startNoBatch) +"с");
            deleteLinesWithoutBP(connection);
            double startBatch = System.currentTimeMillis()*0.001;
            writeToDbWithBatchProcessing(data, connection);
            double endBatch = System.currentTimeMillis()*0.001;
            System.out.println("Время вставки при использовании batch-processing: " +(endBatch-startBatch)+"c");
            return sortAndWrite(connection);
        } catch (SQLException e) {
            System.out.println(SQLE_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    private void writeToDbWithBatchProcessing(File data, Connection connection) {
        String addBatchQuery = "insert into numbers(val) values (?)";
        nbOfLines = countLines(data);
        nbOfBatches = (int) Math.ceil((double) nbOfLines / batchSize);
        try (FileReader fr = new FileReader(data);
             BufferedReader bf = new BufferedReader(fr);
             PreparedStatement ps = connection.prepareStatement(addBatchQuery)) {
            int nbOfLines = countLines(data);
            int linesProcessed = 0;
            for (int i = 0; i < nbOfBatches; i++) {
                for (int j = 0; j < (Math.min(batchSize, nbOfLines - linesProcessed)); j++) {
                    String line = bf.readLine();
                    if (line != null) {
                        ps.setLong(1, Long.parseLong(line));
                        ps.addBatch();
                    } else {
                        break;
                    }
                }
                ps.executeBatch();
            }
        } catch (IOException ioe) {
            System.out.println(IOE_MESSAGE);
            ioe.printStackTrace();
        } catch (SQLException sqle) {
            System.out.println(SQLE_MESSAGE);
            sqle.printStackTrace();
        }
    }


    public File sortAndWrite(Connection connection) {
        String sortingQuery = "select val FROM numbers order by val ASC";
        File outputFile = new File("output.txt");
        try (PreparedStatement preparedStatement = connection.prepareStatement(sortingQuery);
             ResultSet resultSet = preparedStatement.executeQuery();
             FileWriter fw = new FileWriter(outputFile);
             PrintWriter pw = new PrintWriter(fw)) {
            while (resultSet.next()) {
                pw.println(resultSet.getLong("val"));
            }
            pw.flush();
            return outputFile;
        } catch (SQLException e) {
            System.out.println(SQLE_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(IOE_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    private void writeToDBWithoutBatchProcessing(File file,Connection connection) {
        String addQuery = "insert into numbers(val) values (?)";
        try (FileReader fr = new FileReader(file);
             BufferedReader bf = new BufferedReader(fr);
             PreparedStatement ps = connection.prepareStatement(addQuery)) {
            String line;
            while ((line= bf.readLine())!=null){
                ps.setLong(1,Long.parseLong(line));
                ps.execute();
            }
        } catch (IOException e) {
            System.out.println(IOE_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println(SQLE_MESSAGE);
            e.printStackTrace();
        }
    }

    public int countLines(File file) {
        int lines = 0;
        try (Stream<String> fileLines = Files.lines(Path.of(file.getPath()), Charset.defaultCharset())) {
            lines = (int) fileLines.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    private void deleteLinesWithoutBP(Connection connection){
        String deleteQuery = "delete from numbers";
        try(PreparedStatement ps = connection.prepareStatement(deleteQuery)){
            ps.execute();
        } catch (SQLException e) {
            System.out.println(SQLE_MESSAGE);
            e.printStackTrace();
        }
    }



}
