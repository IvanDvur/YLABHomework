package Homework3.FileSort;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class Sorter {
    long[] buffer;
    static String tempFileName = "temp-";
    static int M = 10;
    static int N;

    public File sortFile(File dataFile){
        try {
            N = countLines(dataFile);
            File output = sort(dataFile, N);
            for (int i = 0; i < (int) Math.ceil((double) N / M); i++) {
                Files.deleteIfExists(Path.of(new File(tempFileName + i + ".txt").toURI()));
            }
            return output;
        }catch (IOException e){
            System.out.println("Ошибка ввода-вывода");
            e.printStackTrace();
        }
        return dataFile;
    }

    // Определение количества чанков,деление на чанки, сортировка, запись в temp файлы
    private File sort(File dataFile, long N){
        try {
            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            int buffersCount = (int) Math.ceil((double) N / M);
            int i = 0, j = 0;

            for (i = 0; i < buffersCount; i++) {
                buffer = new long[(int) Math.min(N - i * j, M)];
                //Если размер чанка меньше кол-ва строк в файле, то читаем в несколько проходов
                for (j = 0; j < (M < N ? M : N); j++) {
                    String line = br.readLine();
                    if (line != null) {
                        buffer[j] = Long.parseLong(line);
                    } else {
                        break;
                    }
                }
                Arrays.sort(buffer);
                //После каждого прохода создаём temp файл и пишем в него сортированные прогнанные значения
                writeToTempFile(i, j);
            }
            return merge(buffersCount);
        }catch (IOException e){
            System.out.println("Ошибка ввода-выводы");
            e.printStackTrace();
        }
        return dataFile;
    }

    /**
     * Задаём имя temp файлу(их кол-во и наименование будет соответствовать проходам т.е temp-1,2..i.txt)
     */

    private void writeToTempFile(int i, int j) {
        try {
            FileWriter fw = new FileWriter(tempFileName + i + ".txt");
            PrintWriter pw = new PrintWriter(fw);
            for (int k = 0; k < j; k++) {
                pw.println(buffer[k]);
            }
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File merge(int buffersCount) {
        try {
            long[] minNums = new long[buffersCount];
            BufferedReader[] brs = new BufferedReader[buffersCount];
            File output = new File("output.txt");
            FileWriter fw = new FileWriter(output);
            PrintWriter pw = new PrintWriter(fw);
//
            for (int i = 0; i < buffersCount; i++) {
                brs[i] = new BufferedReader(new FileReader(tempFileName + i + ".txt"));
                String t = brs[i].readLine();
                if (t != null) {
                    minNums[i] = Long.parseLong(t);
                } else {
                    minNums[i] = Long.MAX_VALUE;
                }
            }
//          Данный цикл на каждый итерации проходит по массиву minNums(массив, содержащий current элементы temp фалов)
//          ищет минимальный среди них
//          min - переменная для хранения минимального j-того значения среди minNums
//          minFile - индекс BufferedReader связанного с temp файлом
            for (int i = 0; i < N; i++) {

                long min = minNums[0];
                int minFile = 0;

                for (int j = 0; j < buffersCount; j++) {
                    if (min > minNums[j]) {
                        min = minNums[j];
                        minFile = j;
                    }
                }
                pw.println(min);
                /*В случае исли bufferedReader закончил чтение своего чанка, элементу minNums[minFile] присваивается
                Long.MAX_VALUE для отсева этого чанка из сортировки, иначе меняем минимальное значение на след. значение
                чанка.
                */
                String t = brs[minFile].readLine();
                if (t != null) {
                    minNums[minFile] = Long.parseLong(t);
                }
                else {
                    minNums[minFile] = Long.MAX_VALUE;
                }
            }
            for (int i = 0; i < buffersCount; i++) {
                brs[i].close();
            }
            pw.close();
            fw.close();
            return output;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countLines(File file){
        int lines = 0;
        try(Stream<String> fileLines = Files.lines(Path.of(file.getPath()), Charset.defaultCharset())){
            lines = (int) fileLines.count();
        }catch (Exception e){
            e.printStackTrace();
        }
        return lines;
    }


}
