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

    public List<User> getAllUsers() {
        return new ArrayList<>(appData.getUsers().values());
    }

    public void createUser(User user) {
        appData.getUsers().put(user.getLogin(), user);
    }
}
