package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson04.eventsourcing.Messages.DeleteMessage;
import io.ylab.intensive.lesson04.eventsourcing.Messages.SaveMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageFactory {

    public SaveMessage getSaveMessage(Long personId, String firstName, String lastName, String middleName){
        return new SaveMessage("insert",personId,firstName,lastName,middleName);
    }

    public DeleteMessage getDeleteMessage(Long personId){
        return new DeleteMessage("delete",personId);
    }
}
