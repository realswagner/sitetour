package com.sitetour.sitetourapplication.dto;

public class DashboardStats {

    private long notContacted;
    private long contacting;
    private long awaitingReply;
    private long scheduled;
    private long completed;

    public long getNotContacted() { return notContacted; }
    public void setNotContacted(long notContacted) { this.notContacted = notContacted; }

    public long getContacting() { return contacting; }
    public void setContacting(long contacting) { this.contacting = contacting; }

    public long getAwaitingReply() { return awaitingReply; }
    public void setAwaitingReply(long awaitingReply) { this.awaitingReply = awaitingReply; }

    public long getScheduled() { return scheduled; }
    public void setScheduled(long scheduled) { this.scheduled = scheduled; }

    public long getCompleted() { return completed; }
    public void setCompleted(long completed) { this.completed = completed; }
}