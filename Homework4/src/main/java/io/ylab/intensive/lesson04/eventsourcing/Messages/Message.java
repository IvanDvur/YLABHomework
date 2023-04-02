package io.ylab.intensive.lesson04.eventsourcing.Messages;



public interface Message {


    String getMethod();


    Long getId();


}
