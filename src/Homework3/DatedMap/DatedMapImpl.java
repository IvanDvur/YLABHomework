package Homework3.DatedMap;

import java.util.*;

public class DatedMapImpl implements DatedMap {

    private HashMap<String, String> map;
    private HashMap<String, Date> timestamps;

    public DatedMapImpl() {
        this.map = new HashMap<>();
        this.timestamps = new HashMap<>();
    }

    // Технически присвоить null ключу можно(например, для default значения Map,
// но в этой задаче я решил исключить эту возможность, поправьте, если это недочёт)
    @Override
    public void put(String key, String value) {
        if (key == null) {
            System.out.println("Нельзя присвоить null ключу");
            return;
        }
        if (value == null) {
            map.put(key, value);
            timestamps.put(key, null);
            return;
        }
        map.put(key, value);
        timestamps.put(key, new Date());

    }

    @Override
    public String get(String key) {
        if (key == null) {
            return null;
        }
        return this.map.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    @Override
    public void remove(String key) {
        if (key == null) {
            System.out.println("Нельзя удалить по ключу null");
            return;
        }
        map.remove(key);
        timestamps.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        if (key == null) {
            return null;
        }
        if (map.containsKey(key)) {
            return timestamps.get(key);
        }
        System.out.println("Данный ключ отсутствует");
        return null;
    }

    @Override
    public String toString() {
        return "DatedMapImpl{" +
                "map=" + map +
                '}';
    }
}
