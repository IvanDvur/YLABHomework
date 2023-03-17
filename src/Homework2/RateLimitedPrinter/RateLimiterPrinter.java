package Homework2.RateLimitedPrinter;

public class RateLimiterPrinter {
    /**
     * Поле, хранящее время последнего вызова метода print()
     */
    private long lastPrintTime = 0;
    private int interval;

    public RateLimiterPrinter(int interval) {
        this.interval = interval;
    }

    public void print(String message) {
        if (checkTimeout()) {
            System.out.println(message);
            this.lastPrintTime = System.currentTimeMillis();
        }
    }

    /**
     * @return Таймаут между вызовами методов
     */
    public boolean checkTimeout() {
        return System.currentTimeMillis() - this.lastPrintTime > this.interval;
    }
}
