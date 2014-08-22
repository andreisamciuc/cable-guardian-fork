package co.uk.vonaq.cableguardian.db;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class TestsTable {

    @Inject
    private DBConnector dBConnector;
        
    public int getCableAverage(int cable) throws SQLException {
        int result = 0;
        ResultSet rs = null;
        try {
            rs = dBConnector.executeQuery(
                    "select avg(new_length)from (select new_length from cable_guardian.tests where cable_number = "
                    + cable + " order by test_time desc limit 5)t");
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
    

    public int getLastMeasurement(int cable) throws SQLException {
        int result = 0;
        ResultSet rs = null;
        String query = "select old_length from cable_guardian.tests where cable_number = " + cable + " order by test_time desc limit 1";
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

    public int getAverageCableUntilGood(int cable) throws SQLException {
        //tests average 5 last tests, but only those which where ok.
        int result = 0;
        ResultSet rs = null;
        String query = "select new_length,result from cable_guardian.tests where cable_number = " + cable + " order by test_time desc limit 5";
        try {
            int sum = 0;
            int numberOfResults = 0;
            rs = dBConnector.executeQuery(query);
            while (rs.next()) {
                String status = rs.getString(2);
                if ("OK".equals(status)) {
                    sum += rs.getInt(1);
                    numberOfResults++;
                } else {
                    break;
                }
            }
            if (numberOfResults > 0) {
                result = sum / numberOfResults;
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    public int putMeasurement(int cable, String result, int oldLenth, int newLength) throws SQLException {
        String query = "INSERT INTO cable_guardian.tests (cable_number, result, old_length, new_length) VALUES (?,?,?,?)";
        return dBConnector.executeUpdate(query, ""+cable ,result, ""+oldLenth, ""+newLength);
    }

    public void deleteAllTestForCable(int cable) throws SQLException {
        String query = "delete from cable_guardian.tests where cable_number = " + cable;
        dBConnector.executeUpdate(query);
    }

    public void deleteOldestTests(int toLeave) throws SQLException {
        String query = "DELETE FROM tests WHERE test_id NOT IN (SELECT test_id FROM ( SELECT test_id FROM tests ORDER BY test_id DESC LIMIT " + toLeave + " ) t )";
        dBConnector.executeUpdate(query);
    }

}
