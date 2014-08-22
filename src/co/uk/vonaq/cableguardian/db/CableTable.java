package co.uk.vonaq.cableguardian.db;

import co.uk.vonaq.cableguardian.core.Constants;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CableTable {

    @Inject
    private Constants constants;
    @Inject
    private DBConnector dBConnector;

    public Set<Integer> getTestableCables() throws SQLException {
        Set<Integer> res = new HashSet<>();
        String query = "select cable_number,tolerance,alarm_enabled from cable_guardian.cables where test_enabled = \"Enabled\";";
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

    public int getTolerance(int cable_number) throws SQLException {
        int result = 0;
        String query = "select tolerance from cable_guardian.cables where cable_number = " + cable_number;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            if (rs.next()) {
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

    public boolean getAlarmEnabled(int cable_number) throws SQLException {
        boolean result = false;
        String query = "select alarm_enabled from cable_guardian.cables where cable_number = " + cable_number;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            if (rs.next()) {
                String line = rs.getString(1);
                result = line.contains("Enabled");
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return result;
    }
    
    public boolean isCableSwitch(int cable_number) throws SQLException {
        boolean result = false;
        String query = "select cable_type from cable_guardian.cables where  cable_type = 'Switch' and cable_number = " + cable_number;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            result = rs.next();
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    public String getCableStatus(int cable_number) throws SQLException {
        String result = "";
        String query = "select test_enabled, alarm_enabled from cable_guardian.cables where cable_number = " + cable_number;
        ResultSet cableSet = null;
        try {
            cableSet = dBConnector.executeQuery(query);
            if (cableSet.next()) {
                result += " " + constants.phrases.getString("cable") + " " + cable_number + " ";
                result += " " + constants.phrases.getString("testing") + " ";
                if (cableSet.getString(1).equalsIgnoreCase("Enabled")) {
                    result += " " + constants.phrases.getString("enabled") + " ";
                    result += " " + constants.phrases.getString("fault_reporting") + " ";
                    if (cableSet.getString(2).equalsIgnoreCase("Enabled")) {
                        result += " " + constants.phrases.getString("enabled") + " ";
                    } else {
                        result += " " + constants.phrases.getString("disabled") + " ";
                    }
                } else {
                    result += " " + constants.phrases.getString("disabled") + " ";
                }
            } else {
                return null;
            }
        } finally {
            try {
                cableSet.close();
            } catch (Exception e) {
            }
        }
        return result;
    }
    
    public boolean doesCableExist(int cable_number) throws SQLException {
        String query = "select test_enabled from cable_guardian.cables where cable_number = " + cable_number;
        ResultSet cableSet = null;
        try {
            cableSet = dBConnector.executeQuery(query);
            if (cableSet.next()) {
                return true;
            }
        } finally {
            try {
                cableSet.close();
            } catch (Exception e) {
            }
        }
        return false;
    }

    public void disableLine(int cable_number) throws SQLException {
        String query = "UPDATE cable_guardian.cables SET test_enabled = \"Disabled\" WHERE cable_number = " + cable_number;
        dBConnector.executeUpdate(query);
    }

    public void enableLine(int cable_number) throws SQLException {
        String query = "UPDATE cable_guardian.cables SET test_enabled = \"Enabled\" WHERE cable_number = " + cable_number;
        dBConnector.executeUpdate(query);
    }

    public void configureCable(int cable_number, int admin_id, int tolerance, String test_enabled, String alarm_enabled, String description, String cable_mode) throws SQLException {
        String query = "INSERT INTO cable_guardian.cables (cable_number, group_id, tolerance, test_enabled, alarm_enabled, description, cable_type) VALUES (" + cable_number + "," + admin_id + "," + tolerance + ",'" + test_enabled + "','" + alarm_enabled + "','" + description + "','" + cable_mode+ "')";
        dBConnector.executeUpdate(query);
    }
}
