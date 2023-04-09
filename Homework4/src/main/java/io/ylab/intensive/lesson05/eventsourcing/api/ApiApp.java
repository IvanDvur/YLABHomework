package io.ylab.intensive.lesson05.eventsourcing.api;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    // Тут пишем создание PersonApi, запуск и демонстрацию работы
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    PersonApi personApi = applicationContext.getBean(PersonApi.class);
    // пишем взаимодействие с PersonApi
    personApi.savePerson(2L,"Иван","Иванов","Иванович");
    personApi.savePerson(3L,"Семён","Семёнов","Семёнович");
    personApi.savePerson(3L,"Петр","Петров","Петрович");
    personApi.savePerson(4L,"Светлана","Светланова","Светлановна");
    personApi.savePerson(null,null,null,"Светлановна");
    System.out.println(personApi.findAll());
    personApi.deletePerson(null);
    System.out.println(personApi.findAll());
    System.out.println(personApi.findPerson(3L));
  }
}
