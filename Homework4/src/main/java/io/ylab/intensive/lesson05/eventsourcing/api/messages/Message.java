package io.ylab.intensive.lesson05.eventsourcing.api.messages;



public interface Message {

    String getMethod();

    Long getId();

}
