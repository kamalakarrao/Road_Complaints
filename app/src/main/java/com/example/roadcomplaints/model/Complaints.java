package com.example.roadcomplaints.model;

public class Complaints {
    String complaintId,imageUrl,description,status,agentId;

    public Complaints(String complaintId, String imageUrl, String description, String status, String agentId) {
        this.complaintId = complaintId;
        this.imageUrl = imageUrl;
        this.description = description;
        this.status = status;
        this.agentId = agentId;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}
