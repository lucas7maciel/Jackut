package br.ufal.ic.p2.jackut.Data;

import br.ufal.ic.p2.jackut.Communities.Community;
import br.ufal.ic.p2.jackut.Friendships.Friendship;
import br.ufal.ic.p2.jackut.Messages.Message;
import br.ufal.ic.p2.jackut.Relationships.Relationship;
import br.ufal.ic.p2.jackut.Sessions.Session;
import br.ufal.ic.p2.jackut.Users.User;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppData implements Serializable {
    private Map<String, User> users = new ConcurrentHashMap<>();
    private Map<String, Session> sessions = new ConcurrentHashMap<>();
    private Map<String, Friendship> friendships = new ConcurrentHashMap<>();
    private Map<String, Message> messages = new ConcurrentHashMap<>();
    private Map<String, Community> communities = new ConcurrentHashMap<>();
    private Map<String, Relationship> relationships = new ConcurrentHashMap<>();

    public Map<String, User> getUsers() { return this.users; }
    public Map<String, Session> getSessions() { return this.sessions; }
    public Map<String, Friendship> getFriendShips() { return this.friendships; }
    public Map<String, Message> getMessages() { return this.messages; }
    public Map<String, Community> getCommunities() { return this.communities; }
    public Map<String, Relationship> getRelationships() { return this.relationships; }
}
