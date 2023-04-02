package io.ylab.intensive.lesson05.eventsourcing.api.messages;

import io.ylab.intensive.lesson04.eventsourcing.Messages.Message;
import org.springframework.stereotype.Component;

@Component
public class SaveMessage implements Message {
    private Long id;
    private String method;
    private String firstName;
    private String lastName;
    private String middleName;

    public SaveMessage(String method, Long id, String firstName, String lastName, String middleName) {
        this.method = method;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public SaveMessage() {
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    public Long getId() {
        return this.id;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

}
