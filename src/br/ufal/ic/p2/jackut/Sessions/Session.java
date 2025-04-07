package br.ufal.ic.p2.jackut.Sessions;

import br.ufal.ic.p2.jackut.Users.User;

import java.io.Serializable;

public class Session implements Serializable {
    private final String id;
    private User user;

    public Session(String id, User user) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return this.id;
    }
    public User getUser() { return this.user; }

    public void setUser(User user) { this.user = user; }
}
