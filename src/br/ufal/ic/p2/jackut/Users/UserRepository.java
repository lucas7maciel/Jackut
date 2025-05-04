package br.ufal.ic.p2.jackut.Users;

import br.ufal.ic.p2.jackut.Data.AppData;
import br.ufal.ic.p2.jackut.Data.BaseRepository;

import java.util.ArrayList;
import java.util.List;

public class UserRepository extends BaseRepository {
    public UserRepository(AppData appData) {
        super(appData);
    }

    public User getUserByLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor, informe o login");
        }

        return appData.getUsers().get(login);
    }

    public void saveUser(User user) {
        appData.getUsers().put(user.getLogin(), user);
    }

    public User deleteUser(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor, informe o login");
        }

        cleanupUserData(login);
        return appData.getUsers().remove(login);
    }

    private void cleanupUserData(String login) {

        appData.getCommunities().values().forEach(c -> c.removeMember(login));
        appData.getCommunities().values().removeIf(c -> c.getCreator() == null);

        appData.getRelationships().entrySet()
                .removeIf(e -> e.getKey().contains(login));

        appData.getMessages().values().removeIf(m ->
                m.getFrom().equals(login) || m.getTo().equals(login));

        appData.getSessions().values().removeIf(s -> s.getUser().getLogin().equals(login));
    }
}
