package com.example.schnunltdd;

public class Modelclass {

    String messagess, senderid;
    long timestamp;

    public Modelclass() {
    }

    public Modelclass(String messagess, String senderid, long timestamp) {
        this.messagess = messagess;
        this.senderid = senderid;
        this.timestamp = timestamp;
    }

    public String getMessagess() {
        return messagess;
    }

    public void setMessagess(String messagess) {
        this.messagess = messagess;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
