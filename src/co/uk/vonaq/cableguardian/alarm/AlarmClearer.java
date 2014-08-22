package co.uk.vonaq.cableguardian.alarm;

import co.uk.vonaq.cableguardian.db.AlarmsTable;
import java.sql.SQLException;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AlarmClearer {

    @Inject
    private AlarmsTable alarmsTable;

    public void clearTheAlarm(int cableNumber) throws SQLException {
        alarmsTable.setClr(cableNumber);
    }

    public void acknowledgeTheAlarm(int cableNumber) throws SQLException {
        alarmsTable.setAck(cableNumber);
    }

}
