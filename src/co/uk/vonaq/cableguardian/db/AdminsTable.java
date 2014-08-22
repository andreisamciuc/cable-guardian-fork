package co.uk.vonaq.cableguardian.db;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class AdminsTable {

    @Inject
    private SettingsTable settingsTable;
    @Inject
    private DBConnector dBConnector;

    public int getAdmin(int cable) throws SQLException {
        int result = 0;
        ResultSet rs = null;
        String query = "select admin_id from cable_guardian.cables inner join cable_guardian.admins using (admin_id) where cable_number = " + cable;
        try {
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
        String query = "select mobile from cable_guardian.admins where admin_id = " + admin;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                return rs.getString(1);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public String getAdminsEmail(int admin) throws SQLException {
        String query = "select email from cable_guardian.admins where admin_id = " + admin;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                return rs.getString(1);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return null;
    }
    
    public String getAdminsName(int admin) throws SQLException {
        String query = "select name from cable_guardian.admins where admin_id = " + admin;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                return rs.getString(1);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public void configureAdmin(int admin_id, String name, String mobile, String email, String password) throws SQLException {
        String query = "INSERT INTO cable_guardian.admins (admin_id, name, mobile, email, password) VALUES (" + admin_id + ",'" + name + "','" + mobile + "','" + email + "','" + password +"')";
        dBConnector.executeUpdate(query);
    }

    public int getSystemAdmin() throws SQLException {
        int result;
        String s = settingsTable.getSetting("SystemAdmin");
        if (s.equalsIgnoreCase("")) {
            result = 0;
        }
        result = Integer.parseInt(s);
        return result;
    }

}
