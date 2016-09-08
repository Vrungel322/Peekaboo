package com.peekaboo.model.entity.relations;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.User;
import org.neo4j.ogm.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rtwnk on 9/2/16.
 */
@RelationshipEntity(type = "PENDING_MESSAGES")
public class PendingMessages {

    @GraphId
    private Long relationshipId;
    @StartNode
    private User userfrom;
    @EndNode
    private User userto;
    @Property
    private List<Object> messages = new LinkedList<Object>() {};
    @Property
    private String fromto;
    @Property
    private String typeMessage;


    public PendingMessages() {}

    public PendingMessages(User from, User to,String type,Object message) {
        this.userfrom = from;
        this.userto = to;
        this.fromto = from.getId().toString()+to.getId().toString();
        this.messages.add(message);
        this.typeMessage=type;

    }

    public User getUserfrom() {
        return userfrom;
    }

    public void setUserfrom(User userfrom) {
        this.userfrom = userfrom;
    }

    public User getUserto() {
        return userto;
    }

    public void setUserto(User userto) {
        this.userto = userto;
    }

    public List<Object> getMessages() {
        return messages;
    }

    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }
    public void setType(String type) {
        this.typeMessage = type;
    }
    public String getType() {
        return this.typeMessage;
    }
    /**android devices have to check instance of messages - Text, Audio or Video**/


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PendingMessages that = (PendingMessages) o;

        if (relationshipId != null ? !relationshipId.equals(that.relationshipId) : that.relationshipId != null)
            return false;
        if (userfrom != null ? !userfrom.equals(that.userfrom) : that.userfrom != null) return false;
        if (userto != null ? !userto.equals(that.userto) : that.userto != null) return false;
        return messages != null ? messages.equals(that.messages) : that.messages == null;

    }

    @Override
    public int hashCode() {
        int result = relationshipId != null ? relationshipId.hashCode() : 0;
        result = 31 * result + (userfrom != null ? userfrom.hashCode() : 0);
        result = 31 * result + (userto != null ? userto.hashCode() : 0);
        result = 31 * result + (messages != null ? messages.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PendingMessages{" +
                "relationshipId=" + relationshipId +
                ", userfrom=" + userfrom +
                ", userto=" + userto +
                ", messages=" + messages +
                '}';
    }

    public String getFromto() {
        return fromto;
    }

    public void setFromto(String fromto) {
        this.fromto = fromto;
    }

    public boolean pendingTo(String username) {
        return (this.getUserto().getUsername().equals(username));
    }
}