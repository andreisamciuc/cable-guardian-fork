package co.uk.vonaq.cableguardian.messages;

import co.uk.vonaq.cableguardian.db.DBConnector;
import co.uk.vonaq.cableguardian.db.DBStructureCreator;
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
public class MessageSenderTest {
    @Inject DBStructureCreator dbc;
    @Inject DBConnector dbconnector;
    @Inject MessageSender instance;
    public MessageSenderTest() {
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
     * Test of sendMessage method, of class MessageSender.
     */
    @Test
    public void testSendMessage() throws Exception {
        System.out.println("sendMessage");
        String to = "";
        String subject = "";
        String body = "";
        String mode = "";
        instance.sendMessage(to, subject, body, mode);
    }

    /**
     * Test of sendEmail method, of class MessageSender.
     */
    @Test
    public void testSendEmail() throws SQLException {
        System.out.println("sendEmail");
        String to = "";
        String subject = "";
        String body = "";
        instance.sendEmail(to, subject, body);
    }

    /**
     * Test of sendSMS method, of class MessageSender.
     */
    @Test
    public void testSendSMS() throws Exception {
        System.out.println("sendSMS");
        String number = "";
        String message = "";
        instance.sendSMS(number, message);
    }
    
}
