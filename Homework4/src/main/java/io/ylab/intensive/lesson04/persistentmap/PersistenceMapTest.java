package io.ylab.intensive.lesson04.persistentmap;

import java.sql.SQLException;
import javax.sql.DataSource;

import io.ylab.intensive.lesson04.DbUtil;

public class PersistenceMapTest {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = initDb();
        PersistentMap persistentMap = new PersistentMapImpl(dataSource);
        persistentMap.init("Map1");
        persistentMap.put("Map1_1", "First");
        persistentMap.put("Map1_2", "Second");
        persistentMap.put("Map1_1", "FirstReplace");
        persistentMap.init("ContainsAndGetTest");
        System.out.println(persistentMap.containsKey("TestKey")); //Проверка наличия ключа, до его добавления
        persistentMap.remove("TestKey"); //Сообщение об отсутствии ключа
        persistentMap.put("TestKey","TestValue");
        persistentMap.put("TestKey2","TestValue2");
        persistentMap.put("TestKey3","TestValue3");
        System.out.println(persistentMap.getKeys());
        System.out.println(persistentMap.containsKey("TestKey")); //True - ключ добавили
        System.out.println(persistentMap.get("TestKey"));
        System.out.println(persistentMap.get("Map1_1")); //null - такого ключа нет
        persistentMap.remove("Map1_1"); //Из данного инстанса нельзя удалять ключи других инстансов
        persistentMap.init("Map1"); //переключаемся назад на Map1
        persistentMap.clear();
        persistentMap.put("TestKey","Test key from other map after wipe");
    }

    public static DataSource initDb() throws SQLException {
        String createMapTable = ""
                + "drop table if exists persistent_map; "
                + "CREATE TABLE if not exists persistent_map (\n"
                + "   map_name varchar,\n"
                + "   KEY varchar,\n"
                + "   value varchar\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createMapTable, dataSource);
        return dataSource;
    }
}
