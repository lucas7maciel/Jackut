package br.ufal.ic.p2.jackut.Exceptions;

public class UserAlreadyInCommunity extends RuntimeException{
    public UserAlreadyInCommunity(String message){
        super(message);
    }
}
