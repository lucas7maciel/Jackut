package br.ufal.ic.p2.jackut.Sessions;

import br.ufal.ic.p2.jackut.Data.AppData;
import br.ufal.ic.p2.jackut.Data.BaseRepository;

public class SessionRepository extends BaseRepository {
    public SessionRepository(AppData appData) {
        super(appData);
    }

    public Session getSessionById(int id) {
        if (id == 0) {
            throw new IllegalArgumentException("Id invalido");
        }

        return appData.getSessions().get(Integer.toString(id));
    }

    public void createSession(Session session) {
        appData.getSessions().put(Integer.toString(session.getId()), session);
    }
}
