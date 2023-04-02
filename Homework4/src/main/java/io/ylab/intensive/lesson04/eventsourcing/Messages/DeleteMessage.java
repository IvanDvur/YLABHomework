package io.ylab.intensive.lesson04.eventsourcing.Messages;

public class DeleteMessage implements Message {
    private Long id;
    private String method;

    public DeleteMessage(String method, Long personID) {
        this.method = method;
        this.id = personID;
    }

    public DeleteMessage() {
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public Long getId() {
        return this.id;
    }
}
