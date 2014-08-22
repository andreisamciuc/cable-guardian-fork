package co.uk.vonaq.cableguardian.db;

import co.uk.vonaq.cableguardian.Inject.Log;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GroupsTable {

    @Log
    private Logger logger;
    @Inject
    private DBConnector dBConnector;

    public Set getContacts(int group_id) throws SQLException {
        Set<Integer> res = new LinkedHashSet<Integer>();
        String query = "select contact_1,contact_2,contact_3,contact_4,contact_5,contact_6 from cable_guardian.groups where group_id = " + group_id + ";";
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);

            while (rs.next()) {
                res.add(rs.getInt(1));
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return res;
    }

    public int getAdmin(int group_id) throws SQLException {
        int res = 0;
        String query = "select key_holder from cable_guardian.groups where group_id = " + group_id + ";";
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                res = (rs.getInt(1));
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return res;
    }

    public int getGroup(int cable) throws SQLException {
        int result = 0;
        ResultSet rs = null;
        try {
            String query = "select group_id from cable_guardian.groups inner join cable_guardian.cables using (group_id) where cable_number = " + cable;
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    public String getAdminsSMS(int admin) throws SQLException {
        String result = null;
        ResultSet rs = null;
        try {
            String query = "select mobile from cable_guardian.admins where admin_id = " + admin;
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                result = rs.getString(1);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    public String getAdminsEmail(int admin) throws SQLException {
        String result = null;
        ResultSet rs = null;
        try {
            String query = "select email from cable_guardian.admins where admin_id = " + admin;
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                result = rs.getString(1);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

}
