package co.uk.vonaq.cableguardian.db;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class MockCableTable {

    @Inject
    private DBConnector dBConnector;

    public boolean containsKey(int length) throws SQLException {
        boolean res;
        ResultSet rs = null;
        String query = "select length from cable_guardian.mocks where number = " + length + ";";
        try {
            rs = dBConnector.executeQuery(query);
            res = rs.next();
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
        return res;
    }

    public void put(int number, int length) throws SQLException {
        String query = "INSERT INTO cable_guardian.mocks (number, length) VALUES (" + number + "," + length + ")";
        dBConnector.executeUpdate(query);
    }

    public int get(int number) throws SQLException {
        int length = 0;
        String query = "select length from cable_guardian.mocks where number = " + number + ";";
        ResultSet resultSet = null;
        try {
            resultSet = dBConnector.executeQuery(query);
            if (resultSet.next()) {
                length = resultSet.getInt(1);
            }
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
            }
        }
        return length;
    }

}
