package br.ufal.ic.p2.jackut.Communities;

import br.ufal.ic.p2.jackut.Users.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Community implements Serializable {
    private String name, description;
    private List<User> members;
    private User creator;

    public Community(String name, String description, User creator) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.members = new ArrayList<User>();
    }

    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public List<User> getMembers() { return this.members; }
    public User getCreator() { return this.creator; }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
