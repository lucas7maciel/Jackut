package br.ufal.ic.p2.jackut.Friendships;

import br.ufal.ic.p2.jackut.Users.User;

import java.io.Serializable;

public class Friendship implements Serializable {
    private User askingUser;
    private User requestedUser;
    private FriendshipStatus status;

    public Friendship(User askingUser, User requestedUser) {
        this.askingUser = askingUser;
        this.requestedUser = requestedUser;
        this.status = FriendshipStatus.PENDING;
    }

    public User getAskingUser() {
        return this.askingUser;
    }

    public User getRequestedUser() {
        return this.requestedUser;
    }

    public FriendshipStatus getStatus() { return this.status; }

    public void setStatus(FriendshipStatus status) { this.status = status; }
}

