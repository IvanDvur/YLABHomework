package io.ylab.intensive.lesson05.sqlquerybuilder;

import java.util.List;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SQLQueryExtenderTest {
  public static void main(String[] args) throws Exception{
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    applicationContext.registerShutdownHook();
    SQLQueryBuilder queryBuilder = applicationContext.getBean(SQLQueryBuilder.class);

//    Получение таблиц
    List<String> tables = queryBuilder.getTables();
    System.out.println(tables);
    System.out.println(queryBuilder.queryForTable("some_table"));
    System.out.println(queryBuilder.queryForTable("person"));
    System.out.println(queryBuilder.queryForTable(null));
//     вот так сгенерируем запросы для всех таблиц что есть в БД
    for (String tableName : tables) {
      System.out.println("-------------------------------------------------------------------------------------------");
      System.out.println(queryBuilder.queryForTable(tableName));
    }
  }
}
