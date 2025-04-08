package br.ufal.ic.p2.jackut.Exceptions;

public class SamePersonMessageException extends RuntimeException{
    public SamePersonMessageException(String message){
        super(message);
    }
}
