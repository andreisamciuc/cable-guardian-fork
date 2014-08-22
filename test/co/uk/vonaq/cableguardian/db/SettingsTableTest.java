package co.uk.vonaq.cableguardian.db;

import co.uk.vonaq.cableguardian.inject.GuiceIntegration;
import java.io.IOException;
import javax.inject.Inject;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceIntegration.class)
public class SettingsTableTest {
    @Inject DBConnector dc;
    @Inject SettingsTable settingsTable;
    @Inject DBStructureCreator dbc;
    public SettingsTableTest() throws IOException, Exception {
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
        System.out.println("putMeasurement");
        settingsTable.putSetting("one", "1");
    }
    
    @After
    public void tearDown() {
    }    
      
    /**
     * Test of getSetting method, of class SettingsTable.
     */
    @Test
    public void testGetSetting() throws Exception {
        System.out.println("getSetting");
        assertEquals("1", settingsTable.getSetting("one"));
        assertEquals("", settingsTable.getSetting("two"));
    }
     
    /**
     * Test of getSetting method, of class SettingsTable.
     */
    @Test
    public void testIsCalibrationOn() throws Exception {
        System.out.println("testIsCalibrationOn");
        settingsTable.setCalibrationOn();
        assertTrue( settingsTable.isCalibrationOn());
        settingsTable.setCalibrationOff();
        assertFalse(settingsTable.isCalibrationOn());
    }
    
    /**
     * Test of deleteSetting method, of class SettingsTable.
     */
    @Test
    public void testDeleteSetting() throws Exception {
        System.out.println("deleteSetting");
        settingsTable.deleteSetting("one");
        assertEquals("", settingsTable.getSetting("one"));
    }
    
}
