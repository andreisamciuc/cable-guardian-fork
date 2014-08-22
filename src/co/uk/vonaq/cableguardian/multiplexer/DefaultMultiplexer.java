package co.uk.vonaq.cableguardian.multiplexer;

import co.uk.vonaq.cableguardian.serial.Board;
import co.uk.vonaq.cableguardian.serial.MockBoard;
import co.uk.vonaq.cableguardian.serial.MockBoardWithDatabase;
import co.uk.vonaq.cableguardian.serial.StaticMockBoard;
import co.uk.vonaq.cableguardian.exceptions.BoardDataException;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class DefaultMultiplexer implements Multiplexer {

    @Inject
    private Board kt;
    @Inject
    private MockBoard mb;
    @Inject
    private StaticMockBoard smb;
    @Inject
    private MockBoardWithDatabase mbwd;
    @Inject
    private RelayControl control;
    private int cableNumber;

    @Override
    public void setCable(int number) throws IOException {
        cableNumber = number;
        if (number < 1001) {
            control.disable();
            control.setCable(number);
            control.enable();
        }
    }

    @Override
    public int getLenth() throws IOException, BoardDataException {
        int result = 0;
        if (cableNumber < 1000) {
            result = kt.getLenth();
        } else if (cableNumber < 1020) {
            result = mb.getLenth();
        } else if (cableNumber < 1040) {
            smb.setCable(cableNumber);
            result = smb.getLenth();
        } else if (cableNumber > 1039) {
            mbwd.setCable(cableNumber);
            result = mbwd.getLenth();
        }
        return result;
    }
}

/*Current Multiplex Values:
 boards [1   -1000] realBoard
 boards [1010-1019] random between 710 to 950 meters
 boards [1020-1039] static random values
 boards [1040-..] static random values with database
 */
