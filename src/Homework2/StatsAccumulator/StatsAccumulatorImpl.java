package Homework2.StatsAccumulator;

public class StatsAccumulatorImpl implements StatsAccumulator {
    /**
     * Поля хранящие состояние объекта-аккумулятора для использования постоянного объёма памяти
     */
    private int maxValue = Integer.MIN_VALUE;
    private int minValue = Integer.MAX_VALUE;
    private int sum = 0;
    private int nCount = 0;

    /**
     * Метод добавляющий в аккумулятор число, при этом изменяющий его поля (инкрементация кол-ва элементов,
     * вычисление суммы добавленых элементов, проверка на минимум,максимум)
     * @param value число
     */
    @Override
    public void add(int value) {
        this.nCount++;
        this.sum += value;
        if (value < this.minValue) {
            this.minValue = value;
        }
        if (value > this.maxValue) {
            this.maxValue = value;
        }
    }

    /**
     * Возвращяет значение поля minValue(с проверкой на наличие элементов)
     * @return минимальное значение аккумулятора
     */
    @Override
    public int getMin() {
        if (nCount == 0) {
            System.out.println("В аккумулятор не добавлено ни одного числа");
            return 0;
        }
        return this.minValue;
    }
    /**
     * Возвращяет значение поля maxValue(с проверкой на наличие элементов)
     * @return максимальное значение аккумулятора
     */
    @Override
    public int getMax() {
        if (nCount == 0) {
            System.out.println("В аккумулятор не добавлено ни одного числа");
            return 0;
        }
        return this.maxValue;
    }

    /**
     * Возвращает кол-во добавленых элементов
     * @return кол-во добавленых элементов
     */
    @Override
    public int getCount() {
        return this.nCount;
    }

    /**
     * Возвращает среднее арифметическое всех добавленых элементов с проверкой на наличие элементов
     * @return среднее арифметическое
     */
    @Override
    public Double getAvg() {
        if (this.nCount == 0) {
            System.out.println("В аккумулятор не добавлено ни одного числа");
            return 0.0;
        }
        return this.sum / (double) this.nCount;
    }
}
