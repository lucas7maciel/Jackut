package br.ufal.ic.p2.jackut.Users;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {
    private String name, login, password;
    private final HashMap<String, String> attributes = new HashMap<>();

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public String getName() {return this.name;}

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {return this.password;}

    public String getAttribute(String key) throws Exception {
        if (!this.attributes.containsKey(key)) {
            throw new Exception("Atributo nao preenchido.");
        }

        return this.attributes.get(key);
    }

    public void setAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    @Override
    public String toString() {
        return "User{name='" + this.name + "', login='" + this.login + "', password='" + this.password + "'}";
    }
}
