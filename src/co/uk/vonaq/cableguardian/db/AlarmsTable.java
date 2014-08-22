package co.uk.vonaq.cableguardian.db;

import co.uk.vonaq.cableguardian.core.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class AlarmsTable {

    @Inject
    private Constants constants;
    @Inject
    private DBConnector dBConnector;

    public int putAlarm(String status, int cableNumber, int testId, String result, int switchNumber) throws SQLException {
        String query = "INSERT INTO cable_guardian.alarms(status,cable_number,test_id,result,switch_number)VALUES (?,?,?,?,?)";
        return dBConnector.executeUpdate(query ,status,""+cableNumber,""+testId,result,""+switchNumber);
    }

    public int putAlarm(String status, String result) throws SQLException {
        String query = "INSERT INTO cable_guardian.alarms(status,result)VALUES (?,?)";
        return dBConnector.executeUpdate(query,status,result);
    }

    public boolean getAlarmExists(int cableNumber) throws SQLException {
        boolean result;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery("select * from cable_guardian.alarms where cable_number = " + cableNumber + " AND (status = 'New' || status = 'ACK') limit 1");
            result = rs.next();
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    public String getAlarmString(int alarm_id, boolean addInstructions, boolean alarmIsForSwitch, String adminName) throws SQLException {
        String resultSt = "";
        String query = "select old_length, new_length, alarms.cable_number, test_time, alarms.result from cable_guardian.alarms inner join cable_guardian.tests using (test_id)where alarm_id = " + alarm_id;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            if (rs.next()) {
                resultSt += constants.phrases.getString("cable_guardian_alarm");
                if(!alarmIsForSwitch)resultSt += constants.phrases.getString("cable_number");
                if(!alarmIsForSwitch)resultSt += rs.getString(3);
                resultSt += " ";
                if(alarmIsForSwitch){
                    resultSt += rs.getString(5);
                }else{
                    resultSt += constants.phrases.getString("old_length");
                    resultSt += rs.getString(1);
                    resultSt += " ";
                    resultSt += constants.phrases.getString("new_length");
                    resultSt += rs.getString(2);
                    resultSt += " ";
                }
                resultSt += ". " + constants.phrases.getString("test_time")+" ";
                resultSt += rs.getString(4);
                resultSt += ". ";
                if (addInstructions) {
                    resultSt += constants.phrases.getString("instruction");
                }else{//just add who is in charge
                    resultSt += constants.phrases.getString("responsible_admin_is");
                    resultSt += " ";
                    resultSt += adminName;
                }
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return resultSt;
    }

    public int getCableIdFromAlarmId(int alarm_id) throws SQLException {
        String query = "select cable_number from cable_guardian.alarms where alarm_id = " + alarm_id;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                return rs.getInt(1);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return 0;
    }
       
    public boolean isAlarmForSwitch(int alarm_id) throws SQLException {
        String query = "select switch_number from cable_guardian.alarms where alarm_id = " + alarm_id;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                if(rs.getInt(1)==0)
                    return false;
                else return true;
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return false;
    }

    public void setAck(int cable_number) throws SQLException {
        String query = "UPDATE cable_guardian.alarms SET status='ACK' WHERE cable_number = " + cable_number + " AND status = 'New'";
        dBConnector.executeUpdate(query);
    }

    public void setClr(int cable_number) throws SQLException {
        String query = "UPDATE cable_guardian.alarms SET status='CLR' WHERE cable_number = " + cable_number + " AND status = 'New' || status = 'ACK'";
        dBConnector.executeUpdate(query);
    }

    public void deleteAllAlarmsForCable(int cable) throws SQLException {
        String query = "delete from cable_guardian.alarms where cable_number = " + cable;
        dBConnector.executeUpdate(query);
    }

    public String getStatus(int alarm_id) throws SQLException {
        String resultSt = "";
        String query = "select old_length, new_length, alarms.cable_number, test_time from cable_guardian.alarms inner join cable_guardian.tests using (test_id)where alarm_id = " + alarm_id;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                resultSt += constants.phrases.getString("cable_guardian_alarm");
                resultSt += constants.phrases.getString("cable_number");
                resultSt += rs.getString(3);
                resultSt += " ";
                resultSt += constants.phrases.getString("old_length");
                resultSt += rs.getString(1);
                resultSt += " ";
                resultSt += constants.phrases.getString("new_length");
                resultSt += rs.getString(2);
                resultSt += " ";
                resultSt += constants.phrases.getString("test_time");
                resultSt += rs.getString(4);
                resultSt += " ";
                resultSt += constants.phrases.getString("instruction");
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return resultSt;
    }

    public String getCableStatus(int cable_number) throws SQLException {
        String result = null;
        String query = "select status from cable_guardian.alarms where cable_number = " + cable_number;
        ResultSet cableSet = null;
        try {
            cableSet = dBConnector.executeQuery(query);

            if (cableSet.next()) {
                if (cableSet.getString(1).equalsIgnoreCase("New")) {
                    result = constants.phrases.getString("cable_has_un_alarms");
                } else {
                    result = constants.phrases.getString("cable_has_ack_alarms");
                }
            } else {
                result = constants.phrases.getString("cable_has_no_alarms");
            }
        } finally {
            try {
                cableSet.close();
            } catch (Exception e) {
            }
        }
        return result;
    }
}
