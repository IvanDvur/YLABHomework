package Homework1;

import java.util.Scanner;


public class Stars{
    public static void main(String[] args) throws Exception {
        try(Scanner scanner = new Scanner(System.in)){
            int n = scanner.nextInt(); // Кол-во рядов
            int m = scanner.nextInt(); // Кол-во столбцов
            String template = scanner.next();
            for(int i=0; i< n;i++){
                for (int j = 0; j<m;j++){
                    System.out.print(template);
                }
                System.out.println();
            }
        }
    }

}
