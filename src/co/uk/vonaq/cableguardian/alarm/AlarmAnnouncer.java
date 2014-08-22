package co.uk.vonaq.cableguardian.alarm;

import co.uk.vonaq.cableguardian.core.Constants;
import co.uk.vonaq.cableguardian.db.AdminsTable;
import co.uk.vonaq.cableguardian.db.AlarmsTable;
import co.uk.vonaq.cableguardian.db.GroupsTable;
import co.uk.vonaq.cableguardian.db.SettingsTable;
import co.uk.vonaq.cableguardian.log.DatabaseLogger;
import co.uk.vonaq.cableguardian.messages.MessageSender;
import co.uk.vonaq.cableguardian.utils.RegexUtils;
import java.sql.SQLException;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AlarmAnnouncer {

    @Inject
    private AlarmsTable alarmsTable;
    @Inject
    private AdminsTable adminsTable;
    @Inject
    private GroupsTable groupsTable;
    @Inject
    private MessageSender messageSender;
    @Inject
    private DatabaseLogger dbLogger;
    @Inject
    private Constants constants;
    @Inject
    private SettingsTable settingsTable;

    public void announce(int alarmId) throws SQLException {        
        int cableNumber = alarmsTable.getCableIdFromAlarmId(alarmId);
        int group = groupsTable.getGroup(cableNumber);
        if (group == 0) {
            dbLogger.log(constants.phrases.getString("alarm_rised_on_cable") + " " + cableNumber + " " + constants.phrases.getString("no_group_conf"));
        } else {
            int admin = groupsTable.getAdmin(group);
            String adminName = adminsTable.getAdminsName(admin);
            Set<Integer> contacts = groupsTable.getContacts(group);

            for (Integer i : contacts) {
                inform(i, alarmId, cableNumber, false, adminName);
            }
            inform(admin, alarmId, cableNumber, true, adminName);
        }
    }

    private void inform(int contact, int alarmId, int cableNumber, boolean withInstructions, String mainAdminsName) throws SQLException {
        String adminSMS = adminsTable.getAdminsSMS(contact);
        String adminMail = adminsTable.getAdminsEmail(contact);
        boolean alarmIsForSwitch = alarmsTable.isAlarmForSwitch(alarmId);
        String message = alarmsTable.getAlarmString(alarmId, withInstructions, alarmIsForSwitch, mainAdminsName);
        if (adminSMS != null) {
            messageSender.sendSMS(adminSMS, message);
        }
        if (adminMail != null) {
            if (RegexUtils.validEmail(adminMail)) {
                messageSender.sendEmail(adminMail, constants.phrases.getString("cable_guardian"), message);
            } else {
                dbLogger.log(constants.phrases.getString("email") + " \"" + adminMail + "\" " + constants.phrases.getString("configured_for_line") + " " + cableNumber + " " + constants.phrases.getString("is_not_valid"));
            }
        }
    }
    
    public void announceFailedSms(int alarmId) throws SQLException {
        int admin = adminsTable.getSystemAdmin();
        if (admin != 0) {
            String adminMail = adminsTable.getAdminsEmail(admin);
            String hostname = settingsTable.getSetting("hostname");
            String subject = constants.phrases.getString("cable_guardian") + " " + constants.phrases.getString("host") + hostname + constants.phrases.getString("needs_attention");
            String body = constants.phrases.getString("cable_guardian") + " " + constants.phrases.getString("host") + hostname + constants.phrases.getString("sms_sending_failed");
            messageSender.sendEmail(adminMail, subject, body);
        } else {
            dbLogger.log(constants.phrases.getString("sms_sending_failed") + " " + constants.phrases.getString("no_admin_conf"));
        }
    }
}
