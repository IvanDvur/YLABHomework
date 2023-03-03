package Homework1;

import java.util.Random;
import java.util.Scanner;

public class Guess {
    public static void main(String[] args) throws Exception {
        int number = new Random().nextInt(100);
        int maxAttempts = 10;
        Scanner sc = new Scanner(System.in);

        System.out.println("Я загадал число от 1 до 99. У тебя " + maxAttempts + " попыток угадать.");
        for (int i = 1; i <= maxAttempts; i++) {
            int answer = sc.nextInt();
            if (answer == number) {
                System.out.println("Ты угадал с " + i + " попытки");
                return;
            }
            String s = answer > number ? "Моё число меньше! У тебя " + (maxAttempts - i) + " попыток" :
                                         "Моё число больше! У тебя " + (maxAttempts - i) + " попыток";
            System.out.println(s);
        }
        System.out.println("Ты не угадал");
    }
}
