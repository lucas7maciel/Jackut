package br.ufal.ic.p2.jackut.Relationships;

import br.ufal.ic.p2.jackut.Data.AppData;
import br.ufal.ic.p2.jackut.Data.BaseRepository;
import br.ufal.ic.p2.jackut.Exceptions.RelationshipNotRegisteredException;
import br.ufal.ic.p2.jackut.Exceptions.UserNotRegisteredException;
import br.ufal.ic.p2.jackut.Messages.Message;
import br.ufal.ic.p2.jackut.Messages.MessageRepository;
import br.ufal.ic.p2.jackut.Users.User;
import br.ufal.ic.p2.jackut.Users.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class RelationshipRepository extends BaseRepository {
    MessageRepository messageRepo;
    UserRepository userRepo;

    public RelationshipRepository(AppData appData) {
        super(appData);
        messageRepo = new MessageRepository(appData);
        userRepo = new UserRepository(appData);
    }

    public void save(Relationship relationship) {
        if (relationship == null) {
            throw new IllegalArgumentException("Informe o relationamento");
        }

        appData.getRelationships().put(relationship.getKey(), relationship);

        if (relationship.getType() == RelationshipType.FLIRT) {
            checkNewFlirt(relationship);
        }
    }

    public void checkNewFlirt(Relationship relationship) {
        // Caso o flerte seja mutuo, enviar mensagem para os dois
        Relationship mutual = new Relationship(relationship.getTo(), relationship.getFrom(), relationship.getType());

        if (!appData.getRelationships().containsKey(mutual.getKey())) {
            return;
        }

        User user1 = userRepo.getUserByLogin(relationship.getFrom());
        User user2 = userRepo.getUserByLogin(relationship.getTo());

        if (user1 == null || user2 == null) {
            throw new UserNotRegisteredException("Usuario nao cadastrado.");
        }

        Message message1 = new Message(
                user1.getLogin(),
                String.format("%s e seu paquera - Recado do Jackut.", user2.getName()),
                false
        );

        Message message2 = new Message(
                user2.getLogin(),
                String.format("%s e seu paquera - Recado do Jackut.", user1.getName()),
                false
        );

        messageRepo.saveMessage(message1);
        messageRepo.saveMessage(message2);
    }

    public List<Relationship> getRelationshipsByToAndType(String to, RelationshipType type) {
        if (to == null || type == null) {
            throw new IllegalArgumentException("Por favor, informe tipo e pessoa");
        }

        return appData.getRelationships().values().stream()
                .filter(relationship ->
                        to.equals(relationship.getTo()) &&
                                type.equals(relationship.getType()))
                .collect(Collectors.toList());
    }

    public List<Relationship> filterRelationship(Relationship relationship) {
        // O usuario irá enviar uma variável do tipo Relationship, não necessariamente com todas
        // as propriedades preenchidas, e a função irá filtrar pelas que não estiverem vazias
        List<Relationship> allRelationships = appData.getRelationships().values().stream().toList();

        if (relationship.getFrom() != null) {
            allRelationships = allRelationships.stream().filter(r -> r.getFrom().equals(relationship.getFrom())).toList();
        }

        if (relationship.getTo() != null) {
            allRelationships = allRelationships.stream().filter(r -> r.getTo().equals(relationship.getTo())).toList();
        }

        if (relationship.getType() != null) {
            allRelationships = allRelationships.stream().filter(r -> r.getType().equals(relationship.getType())).toList();
        }

        return allRelationships;
    }

    public boolean isEnemy(String login1, String login2) {
        Relationship relationship = new Relationship(login1, login2, RelationshipType.ENEMY);
        return appData.getRelationships().containsKey(relationship.getKey());
    }

    public Relationship getRelationship(Relationship relationship) {
        if (relationship == null) {
            throw new RelationshipNotRegisteredException("Relacionamento nao existe.");
        }

        return appData.getRelationships().get(relationship.getKey());
    }
}
