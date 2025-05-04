package br.ufal.ic.p2.jackut.Communities;

import br.ufal.ic.p2.jackut.Data.AppData;
import br.ufal.ic.p2.jackut.Data.BaseRepository;
import br.ufal.ic.p2.jackut.Users.User;

import java.util.ArrayList;
import java.util.List;

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

    public List<Community> getCommunitiesByUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<Community> ownedCommunities = appData.getCommunities().values().stream().filter(c ->
                c.getCreator().equals(user)).toList();
        List<Community> joinedCommunities = appData.getCommunities().values().stream().filter(c ->
                c.getMembers().contains(user)).toList();

        List<Community> allCommunities = new ArrayList<>();

        for (int i = 0; i < ownedCommunities.size(); i++) {
            allCommunities.add(ownedCommunities.get(i));
        }

        for (int i = 0; i < joinedCommunities.size(); i++) {
            allCommunities.add(joinedCommunities.get(i));
        }

        return allCommunities;
    }
}