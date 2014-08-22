package co.uk.vonaq.cableguardian.db;

import co.uk.vonaq.cableguardian.inject.GuiceIntegration;
import java.sql.SQLException;
import javax.inject.Inject;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceIntegration.class)
public class AlarmsTableTest {
    @Inject DBConnector dc;
    @Inject AlarmsTable alarmsTable;
    @Inject DBStructureCreator dbc;
    @Inject TestsTable testsTable;
    public AlarmsTableTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception {
        dbc.createDBStructure();
        System.out.println("testSetup");
        System.out.println("putAlarm");
        int result = alarmsTable.putAlarm("New", 1, 12,"ok",0);
        assertEquals(1, result);
        result = alarmsTable.putAlarm("ACK", 1, 13,"good",0);
        assertEquals(2, result);
        result = alarmsTable.putAlarm("CLR", 1, 14,"yes",0);
        assertEquals(3, result);
    }
    
    @After
    public void tearDown() throws SQLException {
    }
    
    /**
     * Test of getAlarmExists method, of class AlarmsTable.
     */
    @Test
    public void testGetAlarmExists() throws Exception {
        System.out.println("getAlarmExists");
        assertEquals(true, alarmsTable.getAlarmExists(1));
        assertFalse(alarmsTable.getAlarmExists(2));
    }

    /**
     * Test of getAlarmString method, of class AlarmsTable.
     */
    @Test
    public void testGetAlarmString() throws Exception {
        System.out.println("getAlarmString");
        int testId = testsTable.putMeasurement(1, "OK", 280, 280);
        alarmsTable.putAlarm("OK", 4, testId, "hi", 0);
        String result = alarmsTable.getAlarmString(4, false, true,"john");
        assertTrue(result.contains("Cable Guardian"));
    }

    /**
     * Test of getCableIdFromAlarmId method, of class AlarmsTable.
     */
    @Test
    public void testGetCableIdFromAlarmId() throws Exception {
        System.out.println("getCableIdFromAlarmId");
        int result = alarmsTable.getCableIdFromAlarmId(3);
        assertEquals(1, result);
    }

    /**
     * Test of setAck method, of class AlarmsTable.
     */
    @Test
    public void testSetAck() throws Exception {
        System.out.println("setAck");
        alarmsTable.setAck(1);
    }

    /**
     * Test of setClr method, of class AlarmsTable.
     */
    @Test
    public void testSetClr() throws Exception {
        System.out.println("setClr");
        alarmsTable.setClr(1);
    }

    /**
     * Test of deleteAllAlarmsForCable method, of class AlarmsTable.
     */
    @Test
    public void testDeleteAllAlarmsForCable() throws Exception {
        System.out.println("deleteAllAlarmsForCable");
        alarmsTable.deleteAllAlarmsForCable(1);
        assertEquals(false, alarmsTable.getAlarmExists(1));
    }

    /**
     * Test of getStatus method, of class AlarmsTable.
     */
    @Test
    public void testGetStatus() throws Exception {
        System.out.println("getStatus");
        String result = alarmsTable.getStatus(1);
        assertEquals("", result);
    }

    /**
     * Test of getCableStatus method, of class AlarmsTable.
     */
    @Test
    public void testGetCableStatus() throws Exception {
        System.out.println("getCableStatus");
        String result = alarmsTable.getCableStatus(1);
        assertTrue(result.contains("Cable has an unacknowledged alarm"));
    }
    
    @Test
    public void testIsAlarmForSwitch() throws Exception {
        System.out.println("testisAlarmForSwitch)");
        int alarmId = alarmsTable.putAlarm("Fail", 1, 1, "", 0);
        boolean result = alarmsTable.isAlarmForSwitch(alarmId);
        assertFalse(result);
        alarmId = alarmsTable.putAlarm("Fail", 1, 1, "", 1);
        result = alarmsTable.isAlarmForSwitch(alarmId);
        assertTrue(true);
    }
    
}
