package co.uk.vonaq.cableguardian.serial;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.db.MockCableTable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

public class MockBoardWithDatabase implements Board {

    @Log
    private Logger logger;
    private final int MAX = 950;
    private final int MIN = 710;
    private int cableNumber;
    @Inject
    private Random r;
    @Inject
    private MockCableTable table;

    @Override
    public int getLenth() throws IOException {
        int length = 0;
        try {
            if (table.containsKey(cableNumber)) {
                length = table.get(cableNumber);
            } else {
                int number = r.nextInt(MAX - MIN + 1) + MIN;
                table.put(cableNumber, number);
                length = number;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "", e);
        }
        return length;
    }

    public void setCable(int cableNumber) {
        this.cableNumber = cableNumber;
    }

    @Override
    public void sendreadRequest() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
