package co.uk.vonaq.cableguardian.db;

import co.uk.vonaq.cableguardian.inject.GuiceIntegration;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceIntegration.class)
public class CableTableTest {
    @Inject
    DBConnector dc;
    @Inject
    CableTable cableTable;
    @Inject 
    DBStructureCreator dbc;
    public CableTableTest() {
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
        System.out.println("configureCable");
        cableTable.configureCable(1, 1, 1, "Enabled", "Enabled", "SomeLine","Cable");
        cableTable.configureCable(2, 2, 10, "Enabled", "Enabled", "SomeLine","Cable");
        cableTable.configureCable(3, 2, 100, "Enabled", "Disabled", "SomeLine","Switch");
        cableTable.configureCable(4, 2, 15, "Enabled", "Hi", "SomeLine","Switch");
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of configureCable method, of class CableTable.
     */
    //@Test
    public void testConfigureCable() throws Exception {
        System.out.println("configureCable");
        cableTable.configureCable(1, 1, 1, "Enabled", "Enabled", "SomeLine","Cable");
        cableTable.configureCable(2, 2, 10, "Enabled", "Enabled", "SomeLine","Cable");
        cableTable.configureCable(3, 2, 100, "Enabled", "Disabled", "SomeLine","Switch");
        cableTable.configureCable(4, 2, 15, "Enabled", "Hi", "SomeLine","Switch");
    }
    
    /**
     * Test of getTestableCables method, of class CableTable.
     */
    @Test
    public void testGetTestableCables() throws Exception {
        System.out.println("getTestableCables");
        Set<Integer> expResult = new HashSet<>();
        expResult.add(1);expResult.add(2);expResult.add(3);expResult.add(4);
        Set<Integer> result = cableTable.getTestableCables();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTolerance method, of class CableTable.
     */
    @Test
    public void testGetTolerance() throws Exception {
        System.out.println("getTolerance");
        assertEquals(1, cableTable.getTolerance(1));
        assertEquals(10, cableTable.getTolerance(2));
        assertEquals(100, cableTable.getTolerance(3));
        assertEquals(15, cableTable.getTolerance(4));
    }

    /**
     * Test of getAlarmEnabled method, of class CableTable.
     */
    @Test
    public void testGetAlarmEnabled() throws Exception {
        System.out.println("getAlarmEnabled");
        assertTrue(cableTable.getAlarmEnabled(1));
        assertTrue(cableTable.getAlarmEnabled(2));
        assertFalse(cableTable.getAlarmEnabled(3));
        assertFalse(cableTable.getAlarmEnabled(4));
    }
    
     /**
     * Test of disableLine method, of class CableTable.
     */
    @Test
    public void testDisableLine() throws Exception {
        System.out.println("disableLine");
        cableTable.disableLine(1);
        cableTable.disableLine(2);
        cableTable.disableLine(3);
        Set<Integer> expResult = new HashSet<>();
        expResult.add(4);
        Set<Integer> result = cableTable.getTestableCables();
        assertEquals(expResult, result);
    }

    /**
     * Test of enableLine method, of class CableTable.
     */
    @Test
    public void testEnableLine() throws Exception {
        System.out.println("enableLine");
        cableTable.enableLine(1);
        cableTable.enableLine(2);
        cableTable.enableLine(3);
        cableTable.disableLine(4);
        Set<Integer> expResult = new HashSet<>();
        expResult.add(1);expResult.add(2);expResult.add(3);
        Set<Integer> result = cableTable.getTestableCables();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCableStatus method, of class CableTable.
     */
    @Test
    //@Ignore
    public void testGetCableStatus() throws Exception {
        System.out.println("getCableStatus");
        
        int cable_number = 1;
        String expResult = "Cable 1  Testing  Enabled  Fault Reporting  Enabled";
        String result = cableTable.getCableStatus(cable_number);
        assertEquals(expResult.trim(), result.trim());
        
        cable_number = 2;
        expResult = "Cable 2  Testing  Enabled  Fault Reporting  Enabled";
        result = cableTable.getCableStatus(cable_number);
        //System.out.println(result);
        assertEquals(expResult.trim(), result.trim());
        
        cable_number = 3;
        expResult = "Cable 3  Testing  Enabled  Fault Reporting  Disabled";
        result = cableTable.getCableStatus(cable_number);
        //System.out.println(result);
        assertEquals(expResult.trim(), result.trim());
        
        cableTable.disableLine(4);
        cable_number = 4;
        expResult = "Cable 4  Testing  Disabled";
        result = cableTable.getCableStatus(cable_number);
        //System.out.println(result);
        assertEquals(expResult.trim(), result.trim());
    }
    
    @Test
    public void testIsCableSwitch() throws Exception {
        System.out.println("testIsCableSwitch()");
        assertFalse(cableTable.isCableSwitch(1));
        assertFalse(cableTable.isCableSwitch(2));
        assertTrue(cableTable.isCableSwitch(3));
        assertTrue(cableTable.isCableSwitch(4));
    }
      
    @Test
    public void testDoesCableExist() throws Exception {
        System.out.println("testIsCableSwitch()");
        assertFalse(cableTable.doesCableExist(22));
        assertFalse(cableTable.doesCableExist(221));
        assertTrue(cableTable.doesCableExist(2));
        assertTrue(cableTable.doesCableExist(4));
    }
    
}
