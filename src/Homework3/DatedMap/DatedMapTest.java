package Homework3.DatedMap;


public class DatedMapTest {
    public static void main(String[] args) throws InterruptedException {
        DatedMap map = new DatedMapImpl();
        map.put("1", "I love Java1");
        map.put("2", "I love Java2");
        map.put("3", "I love Java3");
        map.put("4", "I love Java4");
        map.put("5", null);
        map.remove("3");
        System.out.println(map.keySet());
        System.out.println(map.containsKey("3"));
        for (String item : map.keySet()) {
            System.out.println(item + ": " + map.get(item) + "->" + map.getKeyLastInsertionDate(item));
        }
        Thread.sleep(5000);
        map.put("3", "I lova java3");
        for (String item : map.keySet()) {
            System.out.println(item + ": " + map.get(item) + "->" + map.getKeyLastInsertionDate(item));
        }
    }


}

