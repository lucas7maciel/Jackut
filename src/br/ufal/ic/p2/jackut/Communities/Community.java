package br.ufal.ic.p2.jackut.Communities;

import br.ufal.ic.p2.jackut.Users.User;

import java.io.Serializable;

public class Community implements Serializable {
    private String name, description;
    private User creator;

    public Community(String name, String description, User creator) {
        this.name = name;
        this.description = description;
        this.creator = creator;
    }

    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public User getCreator() { return this.creator; }
}
