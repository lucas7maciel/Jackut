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
import br.ufal.ic.p2.jackut.Relationships.Relationship;
import br.ufal.ic.p2.jackut.Relationships.RelationshipRepository;
import br.ufal.ic.p2.jackut.Relationships.RelationshipType;
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
    RelationshipRepository relationshipRepo = new RelationshipRepository(appData);

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

        if (relationshipRepo.isEnemy(requestedUser.getLogin(), askingUser.getLogin())) {
            throw new InvalidFunctionException(String.format("Funcao invalida: %s e seu inimigo.", requestedUser.getName()));
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

        if (relationshipRepo.isEnemy(user2.getLogin(), user1.getLogin())) {
            return false;
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

        if (relationshipRepo.isEnemy(to, from.getLogin())) {
            User toUser = userRepo.getUserByLogin(to);
            throw new InvalidFunctionException(String.format("Funcao invalida: %s e seu inimigo.", toUser.getName()));
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
        Message message = messageRepo.popMessageByLogin(user.getLogin(), false);

        if (message == null) {
            throw new NoMessagesException("Nao ha recados.");
        }

        return message.getContent();
    }

    // User Story 5
    public void criarComunidade(String id, String name, String description) {
        Session session = sessionRepo.getSessionById(id);

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

        List<User> members = community.getMembers();
        StringBuilder response = new StringBuilder("{");

        response.append(community.getCreator().getLogin());

        if (!members.isEmpty()) {
            response.append(",");
        }

        for(int i = 0; i < members.size(); i++) {
            User member = members.get(i);
            response.append(member.getLogin());

            if (i < members.size() - 1) {
                response.append(",");
            }
        }

        response.append("}");
        return response.toString();
    }

    // User Story 6
    public String getComunidades(String login) {
        if (login == null || login.isEmpty()) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        User user = userRepo.getUserByLogin(login);

        if (user == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        List<Community> communities = communityRepo.getCommunitiesByUser(user);
        StringBuilder response = new StringBuilder("{");

        for (int i = 0; i < communities.size(); i++) {
            Community community = communities.get(i);
            response.append(community.getName());

            if (i < communities.size() - 1) {
                response.append(",");
            }
        }

        response.append("}");
        return response.toString();
    }

    public void adicionarComunidade(String id, String name) {
        Session session = sessionRepo.getSessionById(id);

        if (session == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        Community community = communityRepo.getCommunityByName(name);

        if (community == null) {
            throw new CommunityNotRegisteredException("Comunidade nao existe.");
        }

        User user = session.getUser();

        if (community.getMembers().contains(user) || community.getCreator().equals(user)) {
            throw new UserAlreadyInCommunity("Usuario ja faz parte dessa comunidade.");
        }

        List<User> members = community.getMembers();
        members.add(session.getUser());
        community.setMembers(members);

        communityRepo.saveCommunity(community);
    }

    // User Story 7
    public String lerMensagem(String id) {
        Session session = sessionRepo.getSessionById(id);

        if (session == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        Message message = messageRepo.popMessageByLogin(session.getUser().getLogin(), true);

        if (message == null) {
            throw new NoMessagesException("Nao ha mensagens.");
        }

        return message.getContent();
    }

    public void enviarMensagem(String id, String communityName, String message) {
        Session session = sessionRepo.getSessionById(id);

        if (session == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        Community community = communityRepo.getCommunityByName(communityName);

        if (community == null) {
            throw new CommunityNotRegisteredException("Comunidade nao existe.");
        }

        messageRepo.saveCommunityMessage(community, message);
    }

    // User Story 8
    // * Idolos
    public void adicionarIdolo(String id, String idol) {
        Session session = sessionRepo.getSessionById(id);

        if (session == null || userRepo.getUserByLogin(idol) == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        User user = session.getUser();

        if (user.getLogin().equals(idol)) {
            throw new InvalidRelationshipException("Usuario nao pode ser fa de si mesmo.");
        }

        if (relationshipRepo.isEnemy(idol, user.getLogin())) {
            User idolUser = userRepo.getUserByLogin(idol);
            throw new InvalidFunctionException(String.format("Funcao invalida: %s e seu inimigo.", idolUser.getName()));
        }

        Relationship relationship = new Relationship(user.getLogin(), idol, RelationshipType.IDOL);

        if (relationshipRepo.getRelationship(relationship) != null) {
            throw new RelationshipNotRegisteredException("Usuario ja esta adicionado como idolo.");
        }

        relationshipRepo.save(relationship);
    }

    public boolean ehFa(String login, String idol) {
        Relationship relationship = new Relationship(login, idol, RelationshipType.IDOL);

        if (relationshipRepo.isEnemy(idol, login)) {
            return false;
        }

        return relationshipRepo.getRelationship(relationship) != null;
    }

    public String getFas(String login) {
        User user = userRepo.getUserByLogin(login);

        if (user == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        List<Relationship> relationships = relationshipRepo.getRelationshipsByToAndType(user.getLogin(), RelationshipType.IDOL);
        StringBuilder response = new StringBuilder("{");

        for (int i = 0; i != relationships.size(); i++) {
            Relationship relationship = relationships.get(i);
            response.append(relationship.getFrom());

            if (i != relationships.size() - 1) {
                response.append(",");
            }
        }

        return response.append("}").toString();
    }

    // * Paqueras
    public void adicionarPaquera(String id, String flirt) {
        Session session = sessionRepo.getSessionById(id);

        if (session == null || userRepo.getUserByLogin(flirt) == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        User user = session.getUser();

        if (user.getLogin().equals(flirt)) {
            throw new InvalidRelationshipException("Usuario nao pode ser paquera de si mesmo.");
        }

        if (relationshipRepo.isEnemy(flirt, user.getLogin())) {
            User flirtUser = userRepo.getUserByLogin(flirt);
            throw new InvalidFunctionException(String.format("Funcao invalida: %s e seu inimigo.", flirtUser.getName()));
        }

        Relationship relationship = new Relationship(user.getLogin(), flirt, RelationshipType.FLIRT);

        if (relationshipRepo.getRelationship(relationship) != null) {
            throw new RelationshipNotRegisteredException("Usuario ja esta adicionado como paquera.");
        }

        relationshipRepo.save(relationship);
    }

    public boolean ehPaquera(String id, String flirt) {
        Session session = sessionRepo.getSessionById(id);

        if (session == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        if (relationshipRepo.isEnemy(flirt, session.getUser().getLogin())) {
            return false;
        }

        Relationship relationship = new Relationship(session.getUser().getLogin(), flirt, RelationshipType.FLIRT);

        return relationshipRepo.getRelationship(relationship) != null;
    }

    public String getPaqueras(String id) {
        Session session = sessionRepo.getSessionById(id);

        if (session == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        Relationship searchRelationship = new Relationship(session.getUser().getLogin(), null, RelationshipType.FLIRT);
        List<Relationship> relationships = relationshipRepo.filterRelationship(searchRelationship);

        StringBuilder response = new StringBuilder("{");

        for (int i = relationships.size() - 1; i >= 0; i--) {
            Relationship relationship = relationships.get(i);
            response.append(relationship.getTo());

            if (i > 0) {
                response.append(",");
            }
        }

        return response.append("}").toString();
    }

    // * Inimigos
    public void adicionarInimigo(String id, String enemy) {
        Session session = sessionRepo.getSessionById(id);

        if (session == null || userRepo.getUserByLogin(enemy) == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        if (session.getUser().getLogin().equals(enemy)) {
            throw new InvalidRelationshipException("Usuario nao pode ser inimigo de si mesmo.");
        }

        Relationship relationship = new Relationship(session.getUser().getLogin(), enemy, RelationshipType.ENEMY);

        if (relationshipRepo.getRelationship(relationship) != null) {
            throw new RelationshipAlreadyExistsException("Usuario ja esta adicionado como inimigo.");
        }

        relationshipRepo.save(relationship);
    }

    // User Story 9
    public void removerUsuario(String id) {
        Session session = sessionRepo.getSessionById(id);

        if (session == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        userRepo.deleteUser(session.getUser().getLogin());
    }

    // Geral
    public void zerarSistema() {
        appData.getUsers().clear();
        appData.getSessions().clear();
        appData.getFriendShips().clear();
        appData.getMessages().clear();
        appData.getCommunities().clear();
        appData.getRelationships().clear();
    }
}
