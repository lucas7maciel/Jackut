package br.ufal.ic.p2.jackut.Data;

import br.ufal.ic.p2.jackut.Friendships.Friendship;
import br.ufal.ic.p2.jackut.Sessions.Session;
import br.ufal.ic.p2.jackut.Users.User;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppData implements Serializable {
    private Map<String, User> users = new ConcurrentHashMap<>();
    private Map<String, Session> sessions = new ConcurrentHashMap<>();
    private Map<String, Friendship> friendships = new ConcurrentHashMap<>();

    public Map<String, User> getUsers() { return this.users; }
    public Map<String, Session> getSessions() { return this.sessions; }
    public Map<String, Friendship> getFriendShips() { return this.friendships; }
}
