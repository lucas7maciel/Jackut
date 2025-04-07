package br.ufal.ic.p2.jackut;

import Config.AppConfig;
import br.ufal.ic.p2.jackut.Data.AppData;
import br.ufal.ic.p2.jackut.Data.BaseRepository;
import br.ufal.ic.p2.jackut.Sessions.Session;
import br.ufal.ic.p2.jackut.Sessions.SessionRepository;
import br.ufal.ic.p2.jackut.Users.User;
import br.ufal.ic.p2.jackut.Users.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

public class Jackut {
    private final AppData appData = loadData();

    UserRepository userRepo = new UserRepository(appData);
    SessionRepository sessionRepo = new SessionRepository(appData);

    private static AppData loadData() {
        try {
            File file = new File(AppConfig.DATABASE_FILE);
            return file.exists() ? BaseRepository.loadFromFile(AppConfig.DATABASE_FILE) : new AppData();
        } catch (IOException | ClassNotFoundException e) {
            return new AppData();
        }
    }

    public void saveData() {
        File file = new File(AppConfig.DATABASE_FILE);

        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(file))) {
                oos.writeObject(this.appData);
                System.out.println("Dados salvos com sucesso em: " + file.getAbsolutePath());
            }
        } catch(Exception e) {
            System.err.println("Falha ao salvar dados:");
            throw new RuntimeException("Falha crítica ao salvar dados", e);
        }
    }

    public String getAtributoUsuario(String login, String key) throws IOException {
        User user = userRepo.getUserByLogin(login);

        if (user == null) {
            throw new IOException("Usuario nao cadastrado.");
        }

        if (key.equals("nome")) {
            return user.name;
        }

        if (key.equals("login")) {
            return user.login;
        }

        return null;
    }

    public void criarUsuario(String name, String login, String password) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login invalido.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Senha invalida.");
        }

        if (appData.getUsers().containsKey(login)) {
            throw new IllegalArgumentException("Conta com esse nome ja existe.");
        }

        User user = new User(name, login, password);

        try {
            userRepo.createUser(user);
        } catch(Exception e) {
            System.out.println("Lalala");
        }
    }

    public int abrirSessao(String login, String password) throws Exception {
        if (login == null || login.isEmpty()) {
            throw new Exception("Login ou senha invalidos.");
        }

        User user = userRepo.getUserByLogin(login);

        if (user == null || !user.password.equals(password)) {
            throw new Exception("Login ou senha invalidos.");
        }

        Random random = new Random();
        int id = 0;

        while (id == 0 || sessionRepo.getSessionById(id) != null) {
            id = random.nextInt(Integer.MAX_VALUE);
        }

        sessionRepo.createSession(new Session(id));
        return id;
    }

    public void zerarSistema() {
        appData.getUsers().clear();
        appData.getSessions().clear();
    }
}
