package com.example.demo;

public class Quota {
    private long remaining;
    private long total;

    public Quota() {
        this.total = 0;
        this.remaining = 1000;
    }

    public long getRemaining() {
        return remaining;
    }

    public void setRemaining(long remaining) {
        this.remaining = remaining;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
