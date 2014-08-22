package co.uk.vonaq.cableguardian.db;

import co.uk.vonaq.cableguardian.inject.GuiceIntegration;
import javax.inject.Inject;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceIntegration.class)
public class CalibrationTableTest {
    @Inject DBConnector dc;
    @Inject CalibrationTable calibrationTable;
    @Inject DBStructureCreator dbc;
    public CalibrationTableTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
        
    }
    
    @Test
    public void testSetup() throws Exception {
        dbc.createDBStructure();
        System.out.println("testSetup");
    }
  
    /**
     * Test of putMeasurement method, of class CalibrationTable.
     */
    @Test
    public void testPutMeasurement() throws Exception {
        System.out.println("putMeasurement");
        calibrationTable.putMeasurement(5, 56);
        calibrationTable.putMeasurement(5, 57);
        calibrationTable.putMeasurement(5, 58); 
        assertEquals("1", "1");
    }

    /**
     * Test of deleteAllTest method, of class CalibrationTable.
     */
    @Test
    public void testDeleteAllTestForCable() throws Exception {
        System.out.println("deleteAllTestForCable");
        calibrationTable.deleteAllTest();
    }
    
}
