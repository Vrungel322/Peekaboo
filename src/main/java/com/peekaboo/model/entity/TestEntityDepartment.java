package com.peekaboo.model.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

/**
 * Created by Oleksii on 09.08.2016.
 */
@NodeEntity
public class TestEntityDepartment extends TestAbstractEntity {
    private String name;
    @Relationship(type = "CURRICULUM")
    private Set<TestEntitySubject> subjects;
    public TestEntityDepartment(){

    }
}
