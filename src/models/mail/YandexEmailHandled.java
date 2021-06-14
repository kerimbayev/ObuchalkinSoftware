package models.mail;

import entities.UserEntity;
import models.UserModel;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class YandexEmailHandled implements EmailDao {
    UserEntity user;
    private static EmailDao instance = null;

    public static synchronized EmailDao getInstance() throws MessagingException {
        if (instance == null) {
            instance = new YandexEmailHandled();
        }
        return instance;
    }

    private YandexEmailHandled() throws MessagingException {
        user = UserModel.getInstance().getUser();
        getInbox();
    }

    private Folder getInbox() throws MessagingException {
        Store store = createSession().getStore();
        store.connect("imap.yandex.ru", user.getUsername(), user.getPassword());
        Folder inbox = store.getFolder("Inbox");
        inbox.open(Folder.READ_WRITE);
        return inbox;
    }

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.imap.port", "993");
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imap.ssl.enable", "true");
        props.put("mail.debug", "false");
        Authenticator authenticator = getAuthenticator(user.getUsername(), user.getPassword());
        Session session = Session.getDefaultInstance(props, authenticator);
        session.setDebug(false);
        return session;
    }

    private Authenticator getAuthenticator(String login, String password) {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, password);
            }
        };
    }

    @Override
    public void sendMessage(String toEmail, String subject, String body) throws MessagingException {
        Properties props = System.getProperties();
        String host = "smtp.yandex.ru";
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", user.getUsername());
        props.put("mail.smtp.password", user.getPassword());
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.quitwait", "false");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.debug", "false");
        Session session = Session.getInstance(props);
        session.setDebug(false);
        MimeMessage message = new MimeMessage(session);
        InternetAddress address = new InternetAddress(user.getUsername());
        message.setFrom(address);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject(subject);

        Multipart multipart = new MimeMultipart();
        BodyPart textBody = new MimeBodyPart();
        textBody.setContent(body, "text/plain; charset=utf-8");
        multipart.addBodyPart(textBody);
        message.setContent(multipart);
        Transport transport = session.getTransport("smtp");
        transport.connect(props.getProperty("mail.smtp.host"), user.getUsername(), user.getPassword());
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    @Override
    public ArrayList<Message> getInboxMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        try {
            Folder inbox = getInbox();
            messages = new ArrayList<>(Arrays.asList(inbox.getMessages()));
        } catch (MessagingException ignored) {
        }
        return messages;
    }

    @Override
    public void messagesSetSeen() {
        try {
            Folder inbox = getInbox();
            ArrayList<Message> messages = new ArrayList<>(Arrays.asList(inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false))));
            for (Message message : messages) message.setFlag(Flags.Flag.SEEN, true);
        } catch (MessagingException ignored) {
        }
    }
}

