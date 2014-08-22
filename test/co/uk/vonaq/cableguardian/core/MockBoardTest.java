package co.uk.vonaq.cableguardian.core;

import co.uk.vonaq.cableguardian.inject.GuiceIntegration;
import co.uk.vonaq.cableguardian.serial.Board;
import co.uk.vonaq.cableguardian.serial.MockBoard;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceIntegration.class)
public class MockBoardTest {
    @Inject Board board;
    
    @Test
    public void testGetLenth() throws Exception {
        Board board = new MockBoard();
        board.getLenth();
        //assertEquals("10 x 5 must be 50", 50, tester.multiply(10, 5));
    }
}
