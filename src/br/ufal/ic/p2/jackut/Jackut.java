package br.ufal.ic.p2.jackut;

import Config.AppConfig;
import br.ufal.ic.p2.jackut.Communities.Community;
import br.ufal.ic.p2.jackut.Communities.CommunityRepository;
import br.ufal.ic.p2.jackut.Data.AppData;
import br.ufal.ic.p2.jackut.Data.BaseRepository;
import br.ufal.ic.p2.jackut.Exceptions.*;
import br.ufal.ic.p2.jackut.Friendships.FriendShipRepository;
import br.ufal.ic.p2.jackut.Friendships.Friendship;
import br.ufal.ic.p2.jackut.Friendships.FriendshipStatus;
import br.ufal.ic.p2.jackut.Messages.Message;
import br.ufal.ic.p2.jackut.Messages.MessageRepository;
import br.ufal.ic.p2.jackut.Sessions.Session;
import br.ufal.ic.p2.jackut.Sessions.SessionRepository;
import br.ufal.ic.p2.jackut.Users.User;
import br.ufal.ic.p2.jackut.Users.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Random;

public class Jackut {
    private final AppData appData = loadData();

    UserRepository userRepo = new UserRepository(appData);
    SessionRepository sessionRepo = new SessionRepository(appData);
    FriendShipRepository friendshipRepo = new FriendShipRepository(appData);
    MessageRepository messageRepo = new MessageRepository(appData);
    CommunityRepository communityRepo = new CommunityRepository(appData);

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
            }
        } catch(Exception e) {
            throw new RuntimeException("Falha ao salvar dados" + e);
        }
    }

    // User Story 1
    public void criarUsuario(String name, String login, String password) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login invalido.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Senha invalida.");
        }

        if (appData.getUsers().containsKey(login)) {
            throw new UserAlreadyExistsException("Conta com esse nome ja existe.");
        }

        User user = new User(name, login, password);

        try {
            userRepo.saveUser(user);
        } catch(Exception e) {
            System.err.println(e);
        }
    }

    public String getAtributoUsuario(String login, String key) throws Exception {
        User user = userRepo.getUserByLogin(login);

        if (user == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        if (key.equals("nome")) {
            return user.getName();
        }

        if (key.equals("login")) {
            return user.getLogin();
        }

        return user.getAttribute(key);
    }

    public String abrirSessao(String login, String password) throws Exception {
        if (login == null || login.isEmpty()) {
            throw new InvalidLoginOrPasswordException("Login ou senha invalidos.");
        }

        User user = userRepo.getUserByLogin(login);

        if (user == null || !user.getPassword().equals(password)) {
            throw new InvalidLoginOrPasswordException("Login ou senha invalidos.");
        }

        Random random = new Random();
        String id = null;

        while (id == null || sessionRepo.getSessionById(id) != null) {
            int idValue = random.nextInt(Integer.MAX_VALUE);
            id = Integer.toString(idValue);
        }

        sessionRepo.saveSession(new Session(id, user));
        return id;
    }

    // User Story 2
    public void editarPerfil(String id, String key, String value) throws Exception {
        Session session = sessionRepo.getSessionById(id);

        if (session == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        sessionRepo.updateUser(session, key, value);
    }

    // User Story 3
    public void adicionarAmigo(String id, String login) throws Exception {
        Session session = sessionRepo.getSessionById(id);

        if (session == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        User requestedUser = userRepo.getUserByLogin(login);

        if (requestedUser == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        User askingUser = session.getUser();

        if (askingUser.equals(requestedUser)) {
            throw new SamePersonFriendshipException("Usuario nao pode adicionar a si mesmo como amigo.");
        }

        Friendship friendship = friendshipRepo.getFriendshipByUsers(askingUser, requestedUser);

        if (friendship == null) {
            Friendship newFriendship = new Friendship(askingUser, requestedUser);
            friendshipRepo.saveFriendship(newFriendship);
            return;
        }

        switch (friendship.getStatus()) {
            case PENDING:
                if (friendship.getRequestedUser().equals(askingUser)) {
                    friendship.setStatus(FriendshipStatus.ACCEPTED);
                    friendshipRepo.saveFriendship(friendship);
                } else {
                    throw new FriendshipExistsException("Usuario ja esta adicionado como amigo, esperando aceitacao do convite.");
                }
                break;
            case ACCEPTED:
                throw new FriendshipExistsException("Usuario ja esta adicionado como amigo.");
            default:
                throw new Exception("Status de amizade desconhecido.");
        }
    }

    public boolean ehAmigo(String login1, String login2) throws Exception {
        User user1 = userRepo.getUserByLogin(login1);
        User user2 = userRepo.getUserByLogin(login2);

        if (user1 == null || user2 == null) {
            throw new UserNotRegisteredException("Um ou dois usuarios inexistentes");
        }

        Friendship friendship = friendshipRepo.getFriendshipByUsers(user1, user2);
        return friendship != null && friendship.getStatus().equals(FriendshipStatus.ACCEPTED);
    }

    public String getAmigos(String login) throws Exception {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Login vazio");
        }

        User user = userRepo.getUserByLogin(login);

        if (user == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        List<User> friends = friendshipRepo.getFriendsByUser(user);
        StringBuilder response = new StringBuilder();

        for(User friend : friends) {
            response.append(",").append(friend.getLogin());
        }

        return String.format("{%s}", !response.toString().isEmpty() ? response.substring(1) : "");
    }

    // User Story 4
    public void enviarRecado(String id, String to, String content) throws Exception {
        Session session = sessionRepo.getSessionById(id);

        if (session == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado");
        }

        User from = session.getUser();

        if (from.getLogin().equals(to)) {
            throw new SamePersonMessageException("Usuario nao pode enviar recado para si mesmo.");
        }

        if (userRepo.getUserByLogin(to) == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        Message message = new Message(from.getLogin(), to, content);
        messageRepo.saveMessage(message);
    }

    public String lerRecado(String id) throws Exception {
        Session session = sessionRepo.getSessionById(id);

        if (session == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado");
        }

        User user = session.getUser();
        Message message = messageRepo.popMessageByLogin(user.getLogin());

        if (message == null) {
            throw new NoMessagesException("Nao ha recados.");
        }

        return message.getContent();
    }

    // User Story 5
    public void criarComunidade(String sessionId, String name, String description) {
        Session session = sessionRepo.getSessionById(sessionId);

        if (session == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado");
        }

        if (communityRepo.getCommunityByName(name) != null) {
            throw new SameNameCommunityException("Comunidade com esse nome ja existe.");
        }

        User user = session.getUser(); // Checar usuario nulo
        Community community = new Community(name, description, user);
        communityRepo.saveCommunity(community);
    }

    public String getDescricaoComunidade(String name) {
        Community community = communityRepo.getCommunityByName(name);

        if (community == null) {
            throw new CommunityNotRegisteredException("Comunidade nao existe.");
        }

        return community.getDescription();
    }

    public String getDonoComunidade(String name) {
        Community community = communityRepo.getCommunityByName(name);

        if (community == null) {
            throw new CommunityNotRegisteredException("Comunidade nao existe.");
        }

        return community.getCreator().getLogin();
    }

    public String getMembrosComunidade(String name) {
        Community community = communityRepo.getCommunityByName(name);

        if (community == null) {
            throw new CommunityNotRegisteredException("Comunidade nao existe.");
        }

        return '{' + community.getCreator().getLogin() + '}';
    }

    // Geral
    public void zerarSistema() {
        appData.getUsers().clear();
        appData.getSessions().clear();
        appData.getFriendShips().clear();
        appData.getMessages().clear();
        appData.getCommunities().clear();
    }
}
