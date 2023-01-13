package com.example.demo;

public class ResetRequest {
    private long quota;

    public ResetRequest() {
    }

    public ResetRequest(long quota) {
        this.quota = quota;
    }

    public long getQuota() {
        return quota;
    }
}
