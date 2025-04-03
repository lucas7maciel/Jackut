package br.ufal.ic.p2.jackut;

import easyaccept.EasyAccept;

public class Facade {
    private Jackut jackut;

    public Facade() {
        jackut = new Jackut();
    }

    public void zerarSistema() {
        this.jackut.zerarSistema();
    }
}
