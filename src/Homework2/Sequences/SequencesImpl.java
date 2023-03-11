package Homework2.Sequences;

public class SequencesImpl implements Sequences {


    /**
     * Выводит члены последовательности 2, 4, 6, 8, 10...
     *
     * @param n число членов последовательности для вывода
     */
    @Override
    public void a(int n) {
        int startNum = 0;
        if (n <= 0) {
            System.out.println("Параметр n должен быть больше нуля");
        } else {
            for (int i = 0; i < n; i++) {
                System.out.println(startNum += 2);
            }
        }
    }

    /**
     * Выводит члены последовательности 1, 3, 5, 7, 9...
     *
     * @param n число членов последовательности для вывода
     */
    @Override
    public void b(int n) {
        int startNum = 1;
        if (n <= 0) {
            System.out.println("Параметр n должен быть больше нуля");
        } else {
            for (int i = 0; i < n; i++) {
                System.out.println(startNum);
                startNum += 2;
            }
        }
    }

    /**
     * Выводит члены последовательности  1, 4, 9, 16, 25...
     *
     * @param n число членов последовательности для вывода
     */
    @Override
    public void c(int n) {
        int startNum = 1;
        int deltaNum = 3;
        if (n <= 0) {
            System.out.println("Параметр n должен быть больше нуля");
        } else {
            System.out.println(startNum);
            for (int i = 1; i < n; i++) {
                startNum += deltaNum;
                System.out.println(startNum);
                deltaNum += 2;
            }
        }
    }

    /**
     * Выводит члены последовательности 1, 8, 27, 64, 125...
     *
     * @param n число членов последовательности для вывода
     */
    @Override
    public void d(int n) {
        if (n <= 0) {
            System.out.println("Параметр n должен быть больше нуля");
        } else {
            for (int i = 1; i <= n; i++) {
                System.out.println((int) Math.pow(i, 3));
            }
        }
    }

    /**
     * Выводит члены последовательности 1, -1, 1, -1, 1, -1...
     *
     * @param n число членов последовательности для вывода
     */
    @Override
    public void e(int n) {
        int startNum = -1;
        if (n <= 0) {
            System.out.println("Параметр n должен быть больше нуля");
        } else {
            for (int i = 0; i < n; i++) {
                System.out.println(startNum = -startNum);
            }
        }
    }

    /**
     * Выводит члены последовательности 1, -2, 3, -4, 5, -6...
     *
     * @param n число членов последовательности для вывода
     */
    @Override
    public void f(int n) {
        if (n <= 0) {
            System.out.println("Параметр n должен быть больше нуля");
        } else {
            for (int i = 1; i <= n; i++) {
                System.out.println(i % 2 == 0 ? -i : i);
            }
        }
    }

    /**
     * Выводит члены последовательности 1, -4, 9, -16, 25...
     *
     * @param n число членов последовательности для вывода
     */
    @Override
    public void g(int n) {
        int startNum = 1;
        int deltaNum = 3;
        if (n <= 0) {
            System.out.println("Параметр n должен быть больше нуля");
        } else {
            System.out.println(startNum);
            for (int i = 1; i < n; i++) {
                startNum += deltaNum;
                System.out.println(i % 2 == 0 ? startNum : -startNum);
                deltaNum += 2;
            }
        }
    }

    /**
     * Выводит члены последовательности 1, 0, 2, 0, 3, 0, 4...
     *
     * @param n число членов последовательности для вывода
     */
    @Override
    public void h(int n) {
        int startNum = 1;
        if (n <= 0) {
            System.out.println("Параметр n должен быть больше нуля");
        } else {
            for (int i = 1; i <= n; i++) {
                System.out.println(i % 2 == 0 ? 0 : startNum++);
            }
        }
    }

    /**
     * Выводит члены последовательности 1, 2, 6, 24, 120, 720...
     *
     * @param n число членов последовательности для вывода
     */
    @Override
    public void i(int n) {
        int startNum = 1;
        if (n <= 0) {
            System.out.println("Параметр n должен быть больше нуля");
        } else {
            for (int i = 1; i <= n; i++) {
                System.out.println(startNum = startNum * i);
            }
        }
    }

    /**
     * Выводит члены последовательности 1, 1, 2, 3, 5, 8, 13, 21...
     *
     * @param n число членов последовательности для вывода
     */
    @Override
    public void j(int n) {
        if (n <= 0) {
            System.out.println("Параметр n должен быть больше нуля");
        } else {
            int f1 = 1;
            int f2 = 1;
            int i = 0;
            while (i < n) {
                System.out.println(f1);
                int f3 = f1 + f2;
                f1 = f2;
                f2 = f3;
                i++;
            }
        }
    }
}
