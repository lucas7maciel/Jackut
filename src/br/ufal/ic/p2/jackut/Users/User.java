package br.ufal.ic.p2.jackut.Users;

import java.io.Serializable;

public class User implements Serializable {
    public
    String name, login, password;

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return this.login;
    }

    @Override
    public String toString() {
        return "User{name='" + this.name + "', login='" + this.login + "', password='" + this.password + "'}";
    }
}
