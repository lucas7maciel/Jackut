package br.ufal.ic.p2.jackut.Sessions;

import br.ufal.ic.p2.jackut.Data.AppData;
import br.ufal.ic.p2.jackut.Data.BaseRepository;
import br.ufal.ic.p2.jackut.Users.User;
import br.ufal.ic.p2.jackut.Users.UserRepository;

public class SessionRepository extends BaseRepository {
    UserRepository userRepo;
    public SessionRepository(AppData appData) {
        super(appData);
        userRepo = new UserRepository(appData);
    }

    public Session getSessionById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id invalido");
        }

        return appData.getSessions().get(id);
    }

    public void saveSession(Session session) {
        appData.getSessions().put(session.getId(), session);
    }

    public void updateUser(Session session, String key, String value) {
        User user = session.getUser();
        user.setAttribute(key, value);
        session.setUser(user);

        userRepo.saveUser(user);
        this.saveSession(session);
    }
}
