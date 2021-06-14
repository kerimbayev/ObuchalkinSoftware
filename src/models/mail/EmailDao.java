package models.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.ArrayList;

public interface EmailDao {
    void sendMessage(String toEmail, String subject, String body) throws MessagingException;

    ArrayList<Message> getInboxMessages();

    void messagesSetSeen();
}
