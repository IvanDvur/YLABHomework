package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;

public class MessageFilterApp {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    MessageScheduler scheduler = applicationContext.getBean(MessageScheduler.class);
    File filterRules = new File("D:\\JavaProjects\\YlabHomework\\Homework4\\src\\main\\resources\\Slurs.txt");
    scheduler.start(filterRules);
  }
}
