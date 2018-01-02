package gov.cdc.sdp.cbr.trace.model;

import java.util.Date;

public class TraceLog {
    
    private String description;
    private TraceStatus status;
    private Date createdAt;
    private String cbrId;
    
    public TraceLog( String cbrId, TraceStatus status, String description, Date createdAt) {
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.cbrId = cbrId;
    }

    public String getDescription() {
        return description;
    }

    public TraceStatus getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCbrId() {
        return cbrId;
    }
}
