package Homework2.ComplexNumbers;

public class ComplexNumber {
    /**
     * Поля хранящие действительную и мнимую часть комплексного числа
     */
    private double real;
    private double imaginary;

    /**
     * Конструктор с одним параметром
     * @param real - действительная часть КЧ
     */
    public ComplexNumber(double real) {
        this.real = real;
        this.imaginary = 0.0;
    }

    /**
     * Конструктор с двумя параметрами
     * @param real - действительная часть кч
     * @param imaginary - мнимая часть кч
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Сумма 2 комплексных чисел
     * @param number число с которым производится суммирование
     * @return результат в виде нового объекта
     */
    public ComplexNumber sum(ComplexNumber number) {
        return new ComplexNumber(this.real + number.getReal(), this.imaginary + number.getImaginary());
    }

    /**
     * Разность 2 комплексных чисел
     * @param number число которое будет вычтено
     * @return результат вычитания в виде нового объекта
     */
    public ComplexNumber subtract(ComplexNumber number) {
        return new ComplexNumber(this.real - number.getReal(), this.imaginary - number.getImaginary());
    }

    /**
     * Метод вычисляющий произведение 2 комплексных чисел
     * @param number число на которое будет умножено данный объект
     * @return результат умножения в виде нового объекта
     */
    public ComplexNumber multiply(ComplexNumber number) {
        double real = (this.real * number.getReal() - this.imaginary * number.getImaginary());
        double imaginary = (this.real * number.getImaginary() + this.imaginary * number.getReal());
        return new ComplexNumber(real, imaginary);
    }

    /**
     * Метод возвращающий модуль(в геометрическом смысле длину гипотенузы прямоуг треугольника в координатах real,
     * imaginary)
     * @return модуль комплексного числа
     */
    public double abs() {
        return Math.sqrt(Math.pow(this.real, 2) + Math.pow(this.imaginary, 2));
    }

    /**
     * Геттер, возвращающий действительную часть КЧ
     * @return действительную часть КЧ
     */
    public double getReal() {
        return real;
    }
    /**
     * Геттер, возвращающий мнимую часть КЧ
     * @return мнимую часть КЧ
     */
    public double getImaginary() {
        return imaginary;
    }

    /**
     * toString преобразовывающий объект в строковое представление (с учётом знаков)
     * @return строковое представление числа
     */
    @Override
    public String toString() {
        return this.imaginary > 0 ? this.real + "+" + this.imaginary + "j" : this.real + "" + this.imaginary + "j";
    }
}
