package br.ufal.ic.p2.jackut;

import easyaccept.EasyAccept;

import java.io.IOException;

public class Facade {
    private Jackut jackut;

    public Facade() {
        jackut = new Jackut();
    }

    public String getAtributoUsuario(String login, String key) throws IOException {
        return jackut.getAtributoUsuario(login, key);
    }

    public void criarUsuario(String login, String senha, String nome) {
        jackut.criarUsuario(nome, login, senha);
    }

    public int abrirSessao(String login, String senha) throws Exception {
        return jackut.abrirSessao(login, senha);
    }

    public void zerarSistema() {
        jackut.zerarSistema();
    }

    public void encerrarSistema() {
        jackut.saveData();
    }
}
