package br.ufal.ic.p2.jackut;

import easyaccept.EasyAccept;

import java.io.IOException;

public class Facade {
    private Jackut jackut;

    public Facade() {
        jackut = new Jackut();
    }

    // User Story 1
    public void criarUsuario(String login, String password, String name) {
        jackut.criarUsuario(name, login, password);
    }

    public String getAtributoUsuario(String login, String key) throws Exception {
        return jackut.getAtributoUsuario(login, key);
    }

    public String abrirSessao(String login, String senha) throws Exception {
        return jackut.abrirSessao(login, senha);
    }

    // User Story 2
    public void editarPerfil(String id, String key, String value) throws Exception {
        jackut.editarPerfil(id, key, value);
    }

    // User Story 3
    public void adicionarAmigo(String id, String login) throws Exception {
        jackut.adicionarAmigo(id, login);
    }

    public boolean ehAmigo(String login1, String login2) throws Exception {
        return jackut.ehAmigo(login1, login2);
    }

    public String getAmigos(String login) throws Exception {
        return jackut.getAmigos(login);
    }

    // User Story 4
    public void enviarRecado(String id, String to, String content) throws Exception {
        jackut.enviarRecado(id, to, content);
    }

    public String lerRecado(String id) throws Exception {
        return jackut.lerRecado(id);
    }

    // User Story 5
    public void criarComunidade(String session, String name, String description) throws Exception {
        jackut.criarComunidade(session, name, description);
    }

    public String getDescricaoComunidade(String name) throws Exception {
        return jackut.getDescricaoComunidade(name);
    }

    public String getDonoComunidade(String name) throws Exception {
        return jackut.getDonoComunidade(name);
    }

    public String getMembrosComunidade(String name) throws Exception {
        return jackut.getMembrosComunidade(name);
    }

    // User Story 6
    public String getComunidades(String login) throws Exception {
        return jackut.getComunidades(login);
    }

    public void adicionarComunidade(String id, String name) throws Exception {
        jackut.adicionarComunidade(id, name);
    }

    // User Story 7
    // * Nessa User Story foi implementada a troca de recados entre comunidades,
    //   a classe Message foi reutilizada, porem é citada como 'Mensagens' no
    //   caso de comunidades e 'Recados' no caso de interacoes entre usuarios
    public String lerMensagem(String id) throws Exception {
        return jackut.lerMensagem(id);
    }

    public void enviarMensagem(String id, String communityName, String message) throws Exception {
        jackut.enviarMensagem(id, communityName, message);
    }

    // User Story 8
    // * Idolos
    public void adicionarIdolo(String id, String idol) {
        jackut.adicionarIdolo(id, idol);
    }

    public boolean ehFa(String login, String idol) throws Exception {
        return jackut.ehFa(login, idol);
    }

    public String getFas(String idol) throws Exception {
        return jackut.getFas(idol);
    }

    // * Paqueras
    public void adicionarPaquera(String id, String flirt) {
        jackut.adicionarPaquera(id, flirt);
    }

    public boolean ehPaquera(String id, String flirt) throws Exception {
        return jackut.ehPaquera(id, flirt);
    }

    public String getPaqueras(String id) throws Exception {
        return jackut.getPaqueras(id);
    }

    // * Inimigos
    public void adicionarInimigo(String id, String enemy) {
        jackut.adicionarInimigo(id, enemy);
    }

    // User Story 9
    public void removerUsuario(String id) throws Exception {
        jackut.removerUsuario(id);
    }

    // Geral
    public void zerarSistema() {
        jackut.zerarSistema();
    }

    public void encerrarSistema() {
        jackut.saveData();
    }
}
