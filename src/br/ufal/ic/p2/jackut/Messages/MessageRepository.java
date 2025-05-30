package br.ufal.ic.p2.jackut.Messages;

import br.ufal.ic.p2.jackut.Communities.Community;
import br.ufal.ic.p2.jackut.Data.AppData;
import br.ufal.ic.p2.jackut.Data.BaseRepository;
import br.ufal.ic.p2.jackut.Exceptions.CommunityNotRegisteredException;
import br.ufal.ic.p2.jackut.Users.User;

import java.util.List;
import java.util.Map;

public class MessageRepository extends BaseRepository {
    private int lastId = 1;

    public MessageRepository(AppData appData) {
        super(appData);
    }

    public void saveMessage(Message message) {
        int idValue = lastId++;
        appData.getMessages().put(Integer.toString(idValue), message);
    }

    public void saveCommunityMessage(Community community, String content) {
        if (community == null) {
            throw new CommunityNotRegisteredException("Comunidade nao existe.");
        }

        List<User> members = community.getMembers();

        if (!members.contains(community.getCreator())) {
            members.add(community.getCreator());
        }

        for (User member : members) {
            Message message = new Message(member.getLogin(), content, true);
            this.saveMessage(message);
        }
    }

    public Message popMessageByLogin(String login, boolean fromCommunity) {
        Map<String, Message> messages = appData.getMessages();
        Message firstMessage = null;
        int lesserKey = Integer.MAX_VALUE;

        for (Map.Entry<String, Message> entry : messages.entrySet()) {
            Message message = entry.getValue();
            int keyValue = Integer.parseInt(entry.getKey());

            if (message.getTo().equals(login) && keyValue < lesserKey && message.isFromCommunity() == fromCommunity) {
                firstMessage = message;
                lesserKey = keyValue;
            }
        }

        if (firstMessage != null) {
            appData.getMessages().remove(Integer.toString(lesserKey));
        }

        return firstMessage;
    }
}
