package co.uk.vonaq.cableguardian.db;

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
public class DBConnectorMysqlTest {
    @Inject DBConnector dBConnector;
    @Inject DBStructureCreator dbc;
    public DBConnectorMysqlTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp()  throws Exception {
        dbc.createDBStructure();  
        System.out.println("testSetup");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of executeUpdate method, of class DBConnectorMysql.
     */
    @Test
    public void testExecuteUpdate() throws Exception {
        System.out.println("executeUpdate");
        int expResult = 1;
        String query = "INSERT INTO cable_guardian.alarms(status,result)VALUES (?,?)";
        String status = "New";
        String message = "Message";
        int result = dBConnector.executeUpdate(query,status,message);
        assertEquals(expResult, result);
        
        System.out.println("executeQuery");
        query = "select * from cable_guardian.alarms";
        ResultSet rs = dBConnector.executeQuery(query);
        assertTrue(rs.next());
    }

    /**
     * Test of close method, of class DBConnectorMysql.
     */
    @Test
    public void testRepeat() throws Exception {
        for(int i = 1;i<200;i++){
            System.out.println("repeat");
            String query = "INSERT INTO cable_guardian.alarms(status,result)VALUES (?,?)";
            int result = dBConnector.executeUpdate(query,"New","Message");
            assertEquals(i, result);
            
            String query1 = "select * from cable_guardian.alarms where alarm_id = "+i;
            ResultSet rs = dBConnector.executeQuery(query1);
            assertTrue(rs.next());
        }
    }   
}
