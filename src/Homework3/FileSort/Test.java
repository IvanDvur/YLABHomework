package Homework3.FileSort;

import java.io.File;
import java.io.IOException;


public class Test {

    public static void main(String[] args) throws IOException {

        File dataFile = new Generator().generate("data.txt", 100);
        System.out.println(new Validator(dataFile).isSorted()); // false
        long start = System.currentTimeMillis();
        File sortedFile = new Sorter().sortFile(dataFile);
        long end = System.currentTimeMillis();
        System.out.println(new Validator(sortedFile).isSorted()); // true
        System.out.println(end-start);
    }
}
