package com.ticp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("checkpoint")
public class Checkpoint {

    @Id
    private String id;
    private String content;
    private String userId;
    private Date checkpointDate;

    public Checkpoint() {}

    public Checkpoint(String id, String content, String userId, Date checkpointDate) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.checkpointDate = checkpointDate;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }

    public Date getCheckpointDate() {
        return checkpointDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCheckpointDate(Date checkpointDate) {
        this.checkpointDate = checkpointDate;
    }
}
