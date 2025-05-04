package br.ufal.ic.p2.jackut.Communities;

import br.ufal.ic.p2.jackut.Data.AppData;
import br.ufal.ic.p2.jackut.Data.BaseRepository;
import br.ufal.ic.p2.jackut.Messages.Message;
import br.ufal.ic.p2.jackut.Users.User;

public class CommunityRepository extends BaseRepository {
    public CommunityRepository(AppData appData) {
        super(appData);
    }

    public void saveCommunity(Community community) {
        appData.getCommunities().put(community.getName(), community);
    }

    public Community getCommunityByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor, informe o nome");
        }

        return appData.getCommunities().get(name);
    }
}