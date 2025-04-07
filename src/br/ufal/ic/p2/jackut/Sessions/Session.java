package br.ufal.ic.p2.jackut.Sessions;

import java.io.Serializable;

public class Session implements Serializable {
    private int id;

    public Session(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
