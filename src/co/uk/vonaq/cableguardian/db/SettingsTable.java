package co.uk.vonaq.cableguardian.db;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class SettingsTable {

    @Inject
    private DBConnector dBConnector;

    public String getSetting(String name) throws SQLException {
        String result = "";
        ResultSet rs = null;
        String query = "select setting_value from cable_guardian.settings where setting_name = '" + name + "'";
        try {
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

    public int putSetting(String name, String value) throws SQLException {
        String query = "INSERT INTO cable_guardian.settings (setting_name ,setting_value) VALUES (?,?)ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value)";
        return dBConnector.executeUpdate(query,name, value);
    }

    public void deleteSetting(String name) throws SQLException {
        String query = "delete from cable_guardian.settings where setting_name = ?";
        dBConnector.executeUpdate(query,name);
    }
    
    public boolean isCalibrationOn() throws SQLException{
        return ! getSetting("calibrate").equals("");
    }
    
    public void setCalibrationOn() throws SQLException{
        putSetting("calibrate", "1");
    }
    
    public void setCalibrationOff() throws SQLException{
        deleteSetting("calibrate");
    }

}
