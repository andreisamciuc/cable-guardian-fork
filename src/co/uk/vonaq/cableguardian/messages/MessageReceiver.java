package co.uk.vonaq.cableguardian.messages;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.alarm.AlarmClearer;
import co.uk.vonaq.cableguardian.core.Constants;
import co.uk.vonaq.cableguardian.db.AdminsTable;
import co.uk.vonaq.cableguardian.db.AlarmsTable;
import co.uk.vonaq.cableguardian.db.CableTable;
import co.uk.vonaq.cableguardian.db.GroupsTable;
import co.uk.vonaq.cableguardian.db.SwitchTable;
import co.uk.vonaq.cableguardian.db.SMSTable;
import co.uk.vonaq.cableguardian.db.SettingsTable;
import co.uk.vonaq.cableguardian.log.DatabaseLogger;
import co.uk.vonaq.cableguardian.utils.MessageAction;
import co.uk.vonaq.cableguardian.utils.MessagesParser;
import co.uk.vonaq.cableguardian.utils.RegexUtils;
import co.uk.vonaq.cableguardian.utils.Tuple;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

@Singleton
public class MessageReceiver {

    @Log
    private Logger logger;
    @Inject
    private CableTable cablesTable;
    @Inject
    private SwitchTable nodesTable;
    @Inject
    private AlarmClearer alarmClearer;
    @Inject
    private SMSTable sMSTable;
    @Inject
    private AlarmsTable alarmsTable;
    @Inject
    private DatabaseLogger dbLogger;
    @Inject
    private MessageSender messageSender;
    @Inject
    private GroupsTable groupsTable;
    @Inject
    private AdminsTable adminsTable;
    @Inject
    private Constants constants;
    @Inject
    private SettingsTable settingsTable;

    //todo translate all file
    public void checkEmail() {
        try{
            Properties props = constants.properties;
            String host = settingsTable.getSetting("pop_host");
            String user = settingsTable.getSetting("pop_user");
            String password = settingsTable.getSetting("pop_password");
            
            int port = 995;
            try{
                port = Integer.parseInt(settingsTable.getSetting("pop_port"));
            }catch(Exception e){
                e.printStackTrace();
            }
            props.setProperty("mail.pop3.password",password);
            props.setProperty("mail.pop3.socketFactory",""+port);
            props.setProperty("mail.pop3.port",""+port);
            props.setProperty("mail.pop3.host",host);
            props.setProperty("mail.pop3.user",user);
            
            Session session = Session.getDefaultInstance(props, new Authenticator() {
                
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("username", "password");
                }
            });
        //try {
            // Get a Store object
            Store store = session.getStore("pop3");
            store.connect(host, port, user, password);
            Folder fldr = store.getFolder("INBOX");
            fldr.open(Folder.READ_WRITE);
            int count = fldr.getMessageCount();
            // Message numbers start at 1
            for (int i = 1; i <= count; i++) {
                // Get  a message by its sequence number
                Message m = fldr.getMessage(i);
                //Date date = m.getSentDate();
                Address[] a = m.getFrom();
                String from = MessagesParser.parseFromField(a[0].toString());
                System.out.println("-----" + from + "-----");
                String subj = m.getSubject();
                String body = getContent(m);
                //logger.log(Level.INFO, "Email Received from: " + from);//todo add logging
                dbLogger.logEmail(from, m.getSentDate().toString(), (body.length()>500)?body.substring(0, 500):body);
                //dbLogger.log("Email Received from: " + from);
                m.setFlag(Flags.Flag.DELETED, true);
                processMessageAction(MessagesParser.parse(body), from, "email");
            }
            fldr.close(true);
            store.close();
        } catch (MessagingException | SQLException e) {
            logger.log(Level.SEVERE, "", e);
        }
    }

    private String getContent(Message msg) {
        StringBuilder sb = new StringBuilder();
        try {

            Object objRef = msg.getContent();
            if (!(objRef instanceof Multipart)) {
                //String message = "This Message is not a multipart message!";
                //System.out.println(message);
                return null;
            }
            Multipart mp = (Multipart) msg.getContent();
            Part p = mp.getBodyPart(0);
            InputStream is = p.getInputStream();
            while (is.available() > 0) {
                int c = is.read();
                sb.append((char) c);
            }
        } catch (IOException | MessagingException e) {
            logger.log(Level.SEVERE, "", e);
        }
        return sb.toString();
    }

    public void checkSMS() throws SQLException {
        ResultSet messages = null;
        try {
            messages = sMSTable.getMessages();

            while (messages.next()) {
                int id = messages.getInt(1);
                String number = messages.getString(2);
                Timestamp smsDate = messages.getTimestamp(3);
                Timestamp insertDate = messages.getTimestamp(4);
                String text = messages.getString(5);
                logger.log(Level.INFO, "Sms found: id->" + id + " number->" + number + " smsDate->" + smsDate + " insertDate->" + insertDate + " text->" + text);
                sMSTable.deleteMessage(id);
                dbLogger.logSMS(number, smsDate.toString(), insertDate.toString(), text);
                processMessageAction(MessagesParser.parse(text), number, "sms");
            }
        } finally {
            try {
                messages.close();
            } catch (Exception e) {
            }
        }
    }

    public void checkProcessedOutboxSMS() throws SQLException {
        ResultSet messages = null;
        try {
            messages = sMSTable.getProcessedOutboxMessages();
            while (messages.next()) {
                int id = messages.getInt(1);
                int error = messages.getInt(5);
                if (error != 0) {
                    String number = messages.getString(2);
                    Timestamp insertDate = messages.getTimestamp(3);
                    String text = messages.getString(4);//todo translate
                    String failMessage = "Sms failed: number->" + number + " error code ->" + error + " insertDate->" + insertDate + " text->" + text;
                    logger.log(Level.INFO, failMessage);
                    dbLogger.logFailedSMS(number, error, insertDate.toString(), text);
                    alarmsTable.putAlarm("New", failMessage);
                }
                sMSTable.deleteOutboxMessage(id);
            }
        } finally {
            try {
                messages.close();
            } catch (Exception e) {
            }
        }
    }

    private void processMessageAction(Tuple<MessageAction, Integer, String> map, String number, String mode) throws SQLException {
        if (map == null) {
            return;
        }
        //check if there is no such cable reply
        if (this.cablesTable.doesCableExist(map.second)) {
            //MessageSender sender = new MessageSender(mode);
            if (isPermitted(map.second, number, mode)) {
                MessageAction action = map.first;
                int cable_number = map.second;
                if (cablesTable.getCableStatus(cable_number) == null) {
                    messageSender.sendMessage(number, constants.phrases.getString("cable_guardian"), constants.phrases.getString("cable") + " " + cable_number + " " + constants.phrases.getString("not_configured"), mode);
                } else if (action == MessageAction.ACK) {
                    alarmClearer.acknowledgeTheAlarm(cable_number);
                    messageSender.sendMessage(number, constants.phrases.getString("cable_guardian"), constants.phrases.getString("thank") + " " + action, mode);
                } else if (action == MessageAction.CLR) {
                    alarmClearer.clearTheAlarm(cable_number);
                    messageSender.sendMessage(number, constants.phrases.getString("cable_guardian"), constants.phrases.getString("thank") + " " + action, mode);
                } else if (action == MessageAction.STA) {
                    String result = cablesTable.getCableStatus(cable_number);
                    result += alarmsTable.getCableStatus(cable_number);
                    messageSender.sendMessage(number, constants.phrases.getString("cable_guardian"), result, mode);
                } else if (action == MessageAction.MINUS) {
                    cablesTable.disableLine(cable_number);
                    messageSender.sendMessage(number, constants.phrases.getString("cable_guardian"), constants.phrases.getString("processed"), mode);
                } else if (action == MessageAction.PLUS) {
                    cablesTable.enableLine(cable_number);
                    alarmClearer.clearTheAlarm(cable_number); //fresh start testing
                    messageSender.sendMessage(number, constants.phrases.getString("cable_guardian"), constants.phrases.getString("processed"), mode);
                } else if (action == MessageAction.ADD) {//todo distinct betwean add node and add cable.
                    nodesTable.addNode(cable_number, map.third);
                    //cablesTable.enableLine(cable_number);
                    //alarmClearer.clearTheAlarm(cable_number); //fresh start testing
                    //sender.sendMessage(number, constants.phrases.getString("cable_guardian"), constants.phrases.getString("processed"), mode);
                }
                //now reply to other contacts of the group on that cable that cable was rocessed
                notifyOtherContacts(map, mode);
            } else {
                logger.log(Level.INFO, "reply from this admin  is not permitted" + " " + number);//todo add logging
                dbLogger.log(constants.phrases.getString("reply_not_permited") + " " + number);
            }
        } else {
            dbLogger.log("Action requested for cable: " + map.second + ", but is not configured.");
            logger.log(Level.INFO, "cable: " + map.second + " is not confirured at all!!!");//todo translate;
        }

    }

    private boolean isPermitted(int cableNumber, String number, String mode) throws SQLException {
        boolean res = false;
        int group = groupsTable.getGroup(cableNumber);
        int admin = groupsTable.getAdmin(group);
        if (mode.equals("sms")) {
            String adminSMS = adminsTable.getAdminsSMS(admin);
            if (RegexUtils.areNumbersSame(adminSMS, number)) {
                res = true;
            }
        } else {
            String adminMail = adminsTable.getAdminsEmail(admin);
            if (number.toLowerCase().contains(adminMail.toLowerCase())) {
                res = true;
            }
        }
        return res;
    }
     
    public void notifyOtherContacts(Tuple<MessageAction, Integer, String> map,String mode) throws SQLException {
        int group = groupsTable.getGroup(map.second);
        if (group != 0){
            Set<Integer> contacts = groupsTable.getContacts(group);
            String message = constants.phrases.getString("thank")
                    + constants.phrases.getString("cable")
                    + " " +map.second
                    + " " + constants.phrases.getString("is")
                    + " " +map.first;
            
            for (Integer i : contacts) {
                String sms = adminsTable.getAdminsSMS(i);
                String email = adminsTable.getAdminsEmail(i);
                if (sms != null) {
                    messageSender.sendSMS(sms, message);
                }
                if (email != null) {
                    if (RegexUtils.validEmail(email)) {
                        messageSender.sendEmail(email, constants.phrases.getString("cable_guardian"), message);
                    }
                }
            }
        }
    }
    
}
