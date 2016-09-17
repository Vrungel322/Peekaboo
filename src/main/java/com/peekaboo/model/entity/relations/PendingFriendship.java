package com.peekaboo.model.entity.relations;

import com.peekaboo.model.entity.User;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "REQUEST_FRIENDSHIP")
public class PendingFriendship {

    @GraphId
    private Long relationshipId;
    @Property
    private String fromto;
    @StartNode
    private User userfrom;
    @EndNode
    private User userto;

    public PendingFriendship() {}

    public PendingFriendship(User from, User to) {
        this.userfrom = from;
        this.userto = to;
        this.fromto = from.getId().toString()+to.getId().toString();
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
    public Long getRelationshipId() { return relationshipId; }
    public void setRelationshipId(Long relationshipId) {
        this.relationshipId = relationshipId;
    }
    public String getFromto() {
        return fromto;
    }
    public void setFromto(String fromto) {
        this.fromto = fromto;
    }
}