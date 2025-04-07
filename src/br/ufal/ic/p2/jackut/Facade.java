package br.ufal.ic.p2.jackut;

import easyaccept.EasyAccept;

import java.io.IOException;

public class Facade {
    private Jackut jackut;

    public Facade() {
        jackut = new Jackut();
    }

    public String getAtributoUsuario(String login, String key) throws Exception {
        return jackut.getAtributoUsuario(login, key);
    }

    public void criarUsuario(String login, String senha, String nome) {
        jackut.criarUsuario(nome, login, senha);
    }

    public void editarPerfil(String id, String key, String value) throws Exception {
        jackut.editarPerfil(id, key, value);
    }

    public String abrirSessao(String login, String senha) throws Exception {
        return jackut.abrirSessao(login, senha);
    }

    public void adicionarAmigo(String id, String login) throws Exception {
        jackut.adicionarAmigo(id, login);
    }

    public boolean ehAmigo(String login1, String login2) throws Exception {
        return jackut.ehAmigo(login1, login2);
    }

    public String getAmigos(String login) throws Exception {
        return jackut.getAmigos(login);
    }

    public void enviarRecado(String id, String to, String content) throws Exception {
        jackut.enviarRecado(id, to, content);
    }

    public String lerRecado(String id) throws Exception {
        return jackut.lerRecado(id);
    }

    public void zerarSistema() {
        jackut.zerarSistema();
    }

    public void encerrarSistema() {
        jackut.saveData();
    }
}
