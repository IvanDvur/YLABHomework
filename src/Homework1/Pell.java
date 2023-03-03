package Homework1;

import java.util.Scanner;

public class Pell {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            long p0 = 0;
            long p1 = 1;
            long pn = 0;
            if (n < 2) {
                System.out.println(n);
                return;
            }
            for (int i = 1; i < n; i++) {
                pn = p0 + (2 * p1);
                p0 = p1;
                p1 = pn;
            }
            System.out.println(pn);
        }
    }
}
