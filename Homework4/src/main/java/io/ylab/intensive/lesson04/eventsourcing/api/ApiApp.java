package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.RabbitMQUtil;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    ConnectionFactory connectionFactory = initMQ();
    // Тут пишем создание PersonApi, запуск и демонстрацию работы
    PersonApi sender = new PersonApiImpl(connectionFactory);
    System.out.println(sender.findPerson(1L));
    sender.savePerson(2L,"Иван","Иванов","Иванович");
    sender.savePerson(3L,"Семён","Семёнов","Семёнович");
    sender.savePerson(3L,"Петр","Петров","Петрович");
    sender.savePerson(4L,"Светлана","Светланова","Светлановна");
    System.out.println(sender.findAll());
    sender.deletePerson(3L);
    System.out.println(sender.findAll());
    System.out.println(sender.findPerson(3L));
  }

  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }
}
