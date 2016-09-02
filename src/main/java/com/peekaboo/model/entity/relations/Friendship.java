package com.peekaboo.model.entity.relations;

import com.peekaboo.model.entity.User;
import org.neo4j.ogm.annotation.*;

/**
 * Created by rtwnk on 9/2/16.
 */
@RelationshipEntity(type = "FRIENDS")
public class Friendship {

    @GraphId
    private Long relationshipId;
    @Property
    private String fromto;
    @Property
    private int availibility;
    @StartNode
    private User userfrom;
    @EndNode
    private User userto;

    public Friendship() {}

    public Friendship(User from, User to) {
        this.userfrom = from;
        this.userto = to;
        this.fromto = from.getId().toString()+to.getId().toString();
        this.availibility = 1;
    }

    public int getAvailibility() {
        return availibility;
    }

    public void setAvailibility(int availibility) {
        this.availibility = availibility;
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



    public Long getRelationshipId() {

        return relationshipId;
    }

    public void setRelationshipId(Long relationshipId) {
        this.relationshipId = relationshipId;
    }

    public String getFromto() {
        return fromto;
    }

    public void setFromto(String fromto) {
        this.fromto = fromto;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "relationshipId=" + relationshipId +
                ", fromto='" + fromto + '\'' +
                ", availibility=" + availibility +
                ", userfrom=" + userfrom +
                ", userto=" + userto +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        if (availibility != that.availibility) return false;
        if (relationshipId != null ? !relationshipId.equals(that.relationshipId) : that.relationshipId != null)
            return false;
        if (fromto != null ? !fromto.equals(that.fromto) : that.fromto != null) return false;
        if (userfrom != null ? !userfrom.equals(that.userfrom) : that.userfrom != null) return false;
        return userto != null ? userto.equals(that.userto) : that.userto == null;
    }

    @Override
    public int hashCode() {
        int result = relationshipId != null ? relationshipId.hashCode() : 0;
        result = 31 * result + (fromto != null ? fromto.hashCode() : 0);
        result = 31 * result + availibility;
        result = 31 * result + (userfrom != null ? userfrom.hashCode() : 0);
        result = 31 * result + (userto != null ? userto.hashCode() : 0);
        return result;
    }
}