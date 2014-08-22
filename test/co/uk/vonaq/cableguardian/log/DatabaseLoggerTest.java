package co.uk.vonaq.cableguardian.log;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.db.DBConnector;
import co.uk.vonaq.cableguardian.db.DBStructureCreator;
import co.uk.vonaq.cableguardian.inject.GuiceIntegration;
import java.sql.ResultSet;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceIntegration.class)
public class DatabaseLoggerTest {
    @Log Logger logger;
    @Inject DatabaseLogger instance;
    @Inject DBStructureCreator dbc;
    @Inject DBConnector dbconnector;
    public DatabaseLoggerTest() {
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
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of log method, of class DatabaseLogger.
     */
    @Test
    public void testLog() throws Exception {
        System.out.println("log");
        instance.log("hi");
        ResultSet rs = dbconnector.executeQuery("select * from log");
        assertTrue(rs.next());
    }

    /**
     * Test of logSMS method, of class DatabaseLogger.
     */
    @Test
    public void testLogSMS() throws Exception {
        System.out.println("logSMS");
        instance.logSMS("", "", "", "");
        ResultSet rs = dbconnector.executeQuery("select * from log");
        assertTrue(rs.next());
        assertFalse(rs.next());
    }

    /**
     * Test of logFailedSMS method, of class DatabaseLogger.
     */
    @Test
    public void testLogFailedSMS() throws Exception {
        System.out.println("logFailedSMS");
        instance.logFailedSMS("", 8, "", "");
        ResultSet rs = dbconnector.executeQuery("select * from log");
        assertTrue(rs.next());
        assertFalse(rs.next());
    }

    /**
     * Test of logSMSOut method, of class DatabaseLogger.
     */
    @Test
    public void testLogSMSOut() throws Exception {
        System.out.println("logSMSOut");
        instance.logSMSOut("", "");
        ResultSet rs = dbconnector.executeQuery("select * from log");
        assertTrue(rs.next());
        assertFalse(rs.next());
        
    }
    
}
