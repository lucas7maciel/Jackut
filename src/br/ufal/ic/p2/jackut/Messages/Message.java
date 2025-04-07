package br.ufal.ic.p2.jackut.Messages;

import java.io.Serializable;

public class Message implements Serializable {
    private String from, to;
    private String content;

    public Message(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getFrom() { return this.from; }
    public String getTo() { return this.to; }
    public String getContent() { return this.content; }
}
