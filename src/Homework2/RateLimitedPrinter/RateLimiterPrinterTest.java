package Homework2.RateLimitedPrinter;

public class RateLimiterPrinterTest {
    /**
     * Печать сообщений с частотой 1 раз в 2 секунды
     */
    public static void main(String[] args) {
        RateLimiterPrinter rlp = new RateLimiterPrinter(2000);
        while (true) {
            rlp.print("some message");
        }
    }
}
