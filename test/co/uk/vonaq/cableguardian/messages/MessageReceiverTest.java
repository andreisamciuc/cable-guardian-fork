package co.uk.vonaq.cableguardian.messages;

import co.uk.vonaq.cableguardian.db.DBConnector;
import co.uk.vonaq.cableguardian.db.DBStructureCreator;
import co.uk.vonaq.cableguardian.inject.GuiceIntegration;
import java.sql.ResultSet;
import javax.inject.Inject;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceIntegration.class)
public class MessageReceiverTest {
    @Inject DBStructureCreator dbc;
    @Inject DBConnector dbconnector;
    @Inject MessageReceiver instance;
    @Inject MessageReceiver messageReceiver;
    
    public MessageReceiverTest() {
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
     * Test of checkEmail method, of class MessageReceiver.
     */
    @Test
    public void testCheckEmail() {
        System.out.println("checkEmail");
        instance.checkEmail();
    }

    /**
     * Test of checkSMS method, of class MessageReceiver.
     */
    @Test
    public void testCheckSMS() throws Exception {
        System.out.println("checkSMS");
        instance.checkSMS();
    }

    /**
     * Test of checkProcessedOutboxSMS method, of class MessageReceiver.
     */
    @Test
    public void testCheckProcessedOutboxSMS() throws Exception {
        dbconnector.executeUpdate("insert into outbox (number,text,processed) values(?,?,?)", "333444","Hi","1");
        System.out.println("checkProcessedOutboxSMS");
        instance.checkProcessedOutboxSMS();
        messageReceiver.checkProcessedOutboxSMS();
        String message = "sms failed";
        ResultSet rs = dbconnector.executeQuery("select * from log");
        String res = null;
        if (rs.next()) {
            res = rs.getString(3);
        }
        System.out.println(res);
        assertTrue(res.contains(message));
    }
    
}
