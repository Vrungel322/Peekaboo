package com.peekaboo.model.entity;


import org.neo4j.ogm.annotation.GraphId;

public class Storage {
    @GraphId
    private Long id;

    private String fileName;

    private String filePath;

    public Storage(){

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
        final StringBuilder sb = new StringBuilder("VerificationToken{");
        sb.append("id='").append(id).append('\'');
        sb.append("path='").append(filePath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
