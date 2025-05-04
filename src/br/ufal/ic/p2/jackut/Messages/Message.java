package br.ufal.ic.p2.jackut.Messages;

import java.io.Serializable;

public class Message implements Serializable {
    private String from, to;
    private String content;
    private boolean fromCommunity;

    public Message(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.fromCommunity = false;
    }

    public Message(String to, String content, boolean fromCommunity) {
        this.to = to;
        this.content = content;
        this.fromCommunity = fromCommunity;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public String getContent() {
        return this.content;
    }

    public boolean isFromCommunity() {
        return this.fromCommunity;
    }
}
