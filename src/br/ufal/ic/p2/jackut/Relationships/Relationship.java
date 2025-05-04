package br.ufal.ic.p2.jackut.Relationships;

import java.io.Serializable;

public class Relationship implements Serializable {
    public String from, to;
    public RelationshipType type;

    public Relationship(String from, String to, RelationshipType type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public String getKey() {
        return String.format("%s:%s:%s", this.getFrom(), this.getTo(), this.getType().name());
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public RelationshipType getType() {
        return type;
    }
}
