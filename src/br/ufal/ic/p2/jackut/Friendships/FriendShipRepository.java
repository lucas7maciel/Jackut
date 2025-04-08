package br.ufal.ic.p2.jackut.Friendships;

import br.ufal.ic.p2.jackut.Data.AppData;
import br.ufal.ic.p2.jackut.Data.BaseRepository;
import br.ufal.ic.p2.jackut.Users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendShipRepository extends BaseRepository {
    private int lastId = 1;

    public FriendShipRepository(AppData appData) {
        super(appData);
    }

    public Friendship getFriendshipByUsers(User user1, User user2) {
        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("Usuario nao pode ser nulo");
        }

        for (Friendship friendship : appData.getFriendShips().values()) {
            User asking = friendship.getAskingUser();
            User requested = friendship.getRequestedUser();

            if ((asking.equals(user1) && requested.equals(user2)) ||
                    (asking.equals(user2) && requested.equals(user1))) {
                return friendship;
            }
        }

        return null;
    }

    public List<User> getFriendsByUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuario nao pode ser nulo");
        }

        List<User> friends = new ArrayList<>(appData.getFriendShips().size());

        for (Friendship friendship : appData.getFriendShips().values()) {
            if (!friendship.getStatus().equals(FriendshipStatus.ACCEPTED)) continue;

            User asking = friendship.getAskingUser();
            User requested = friendship.getRequestedUser();

            if (friends.contains(asking) || friends.contains(requested)) continue;

            if (asking.equals(user)) {
                friends.add(requested);
            } else if (requested.equals(user)) {
                friends.add(asking);
            }
        }

        return friends;
    }

    public void saveFriendship(Friendship friendship) {
        String id = Integer.toString(lastId++);
        appData.getFriendShips().put(id, friendship);
    }
}
