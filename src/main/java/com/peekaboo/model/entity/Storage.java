package com.peekaboo.model.entity;


import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Storage {

    @GraphId
    private Long id;

    private String fileName;

    private String filePath;

    public Storage(){}

    public Storage(String fileName, String filePath){
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Storage{");
        sb.append("id='").append(id).append('\'');
        sb.append("fileName='").append(fileName).append('\'');
        sb.append("path='").append(filePath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
