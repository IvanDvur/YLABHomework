package Homework2.StatsAccumulator;

public class StatsAccumulatorTest {
    public static void main(String[] args) {
        StatsAccumulatorImpl sa = new StatsAccumulatorImpl();
        sa.add(1);
        sa.add(2);
        sa.add(3);
        sa.add(4);
        sa.add(5);
        sa.add(6);
        sa.add(7);
        sa.add(8);
        sa.add(9);
        sa.add(10);
        sa.add(77);
        sa.add(31);
        System.out.println(sa.getMax());
        System.out.println(sa.getMin());
        System.out.println(sa.getAvg());
        System.out.println(sa.getCount());

    }


}
