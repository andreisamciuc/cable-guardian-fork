package co.uk.vonaq.cableguardian.db;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;

@Singleton
public class CalibrationTable {

    @Inject
    private DBConnector dBConnector;

    public int putMeasurement(int cable_number, int length) throws SQLException {
        String query = "INSERT INTO cable_guardian.calibration (cable_number, length) VALUES (" + cable_number + "," + length + ")";
        return dBConnector.executeUpdate(query);
    }

    public void deleteAllTest() throws SQLException {
        String query = "delete from cable_guardian.calibration";
        dBConnector.executeUpdate(query);
    }

}
