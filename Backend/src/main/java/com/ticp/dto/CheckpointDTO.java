package com.ticp.dto;

import java.util.Date;

public class CheckpointDTO {
    private String id;
    private String username;
    private String content;
    private Date checkpointDate;
    private String formattedCheckpointDate;

    public CheckpointDTO() {}

    public CheckpointDTO(String id, String username, String content, Date checkpointDate, String formattedCheckpointDate) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.checkpointDate = checkpointDate;
        this.formattedCheckpointDate = formattedCheckpointDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCheckpointDate(Date checkpointDate) {
        this.checkpointDate = checkpointDate;
    }

    public void setFormattedCheckpointDate(String formattedCheckpointDate) {
        this.formattedCheckpointDate = formattedCheckpointDate;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public Date getCheckpointDate() {
        return checkpointDate;
    }

    public String getFormattedCheckpointDate() {
        return formattedCheckpointDate;
    }
}
