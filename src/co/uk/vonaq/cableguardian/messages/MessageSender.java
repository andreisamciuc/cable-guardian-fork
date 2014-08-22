package co.uk.vonaq.cableguardian.messages;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.core.Constants;
import co.uk.vonaq.cableguardian.db.SMSTable;
import co.uk.vonaq.cableguardian.db.SettingsTable;
import co.uk.vonaq.cableguardian.log.DatabaseLogger;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Singleton
public class MessageSender {

    @Log
    private Logger logger;
    @Inject
    private SMSTable sMSTable;
    @Inject
    private DatabaseLogger dbLogger;
    @Inject
    private Constants constants;
    @Inject
    private SettingsTable settingsTable;

    public void sendMessage(String to, String subject, String body, String mode) throws SQLException {
        if (mode.equalsIgnoreCase("email")) {
            sendEmail(to, subject, body);
        } else {
            sendSMS(to, body);
        }
    }

    public void sendEmail(String to, String subject, String body) throws SQLException {
        logger.log(Level.INFO, "EmailSender class received email to send to email-> " + to + " message-> " + body);
        Properties props = constants.properties;
        String host = settingsTable.getSetting("smtp_host");
        String from = settingsTable.getSetting("smtp_user");
        String pass = settingsTable.getSetting("smtp_password");
        String port = settingsTable.getSetting("smtp_port");
        
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.user", from);
        props.setProperty("mail.smtp.password", pass);
        props.setProperty("mail.smtp.port", port);
  
        Session session = Session.getInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress toAddress = new InternetAddress(to);
            message.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            logger.log(Level.SEVERE, "", ae);
        } catch (MessagingException me) {
            logger.log(Level.SEVERE, "", me);
        }
        dbLogger.logEmailOut(to, subject, body);
    }

    public void sendSMS(String number, String message) throws SQLException {
        sMSTable.sendMessage(number, message);
        dbLogger.logSMSOut(number, message);
        logger.log(Level.INFO, "SMSSender class received message to send to number-> " + number + " message-> " + message);
    }
}
