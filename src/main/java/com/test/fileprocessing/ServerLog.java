package com.test.fileprocessing;

public class ServerLog {
    private String id;
    private String state;
    private long timestamp;
    private String type;
    private String host;

    public ServerLog() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String toString() {
        return "{id='" + this.id + '\'' + ", state='" + this.state + '\'' + ", timestamp=" + this.timestamp + ", type='" + this.type + '\'' + ", host='" + this.host + '\'' + '}';
    }
}
