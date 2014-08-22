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
public class SwitchTableTest {
    @Inject
    DBConnector dc;
    @Inject
    SwitchTable switchTable;
    @Inject 
    DBStructureCreator dbc;
    @Inject
    TestsTable testsTable;
    
    public SwitchTableTest() {
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
        testAddNode();
    }
    
    @After
    public void tearDown() {
    }
 
    /**
     * Test of addNode method, of class SwitchTable.
     */
    //@Test  Commented out. anyway will be called from test setup
    public void testAddNode() throws Exception {
        System.out.println("addNode");
        int result;
        result = testsTable.putMeasurement(1, "OK", 280, 280);
        switchTable.addNode(1, "11 street");
        result = testsTable.putMeasurement(1, "OK", 260, 260);
        switchTable.addNode(1, "at station");
        result = testsTable.putMeasurement(1, "OK", 100, 100);
        switchTable.addNode(1, "last metres");
        result = testsTable.putMeasurement(1, "OK", 50 , 50 );
        switchTable.addNode(1, "branch");
        result = testsTable.putMeasurement(1, "Fail Continue", 0, 280);
        switchTable.addNode(1, "branch2");
        result = testsTable.putMeasurement(1, "fail tolerance exceeded difference:128", 278, 150);
        switchTable.addNode(1, "111 street");
    }
 
    /**
     * Test of getNodesForCable method, of class SwitchTable.
     */
    @Test
    public void testGetTestableNodes() throws Exception {
        System.out.println("getTestableNodes");
        Set<Integer> expResult = new HashSet<>();
        expResult.add(1);expResult.add(2);expResult.add(3);expResult.add(4);expResult.add(5);expResult.add(6);
        Set<Integer> result = switchTable.getNodesForCable(1);
        assertEquals(expResult, result);
    }

    /**
     * Test of deleteAllNodesForCable method, of class SwitchTable.
     */
    @Test
    public void testDeleteAllNodesForCable() throws Exception {
        System.out.println("deleteAllNodesForCable");
        switchTable.deleteAllNodesForCable(1);
        Set<Integer> expResult = new HashSet<>();
        //expResult.add(1);expResult.add(2);expResult.add(3);expResult.add(4);
        Set<Integer> result = switchTable.getNodesForCable(1);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetNodeForCableByDistance() throws Exception {
        System.out.println("testGetNodeForCableByDistance()");
        int o = switchTable.getNodeForCableByDistance(1,4534);
        assertEquals(1,o);
        testsTable.putMeasurement(1, "OK", 2800, 2800);
        testsTable.putMeasurement(1, "OK", 2800, 2800);
        testsTable.putMeasurement(1, "OK", 2800, 2800);
        testsTable.putMeasurement(1, "OK", 2800, 2800);
        switchTable.addNode(1, "11 street");
        o = switchTable.getNodeForCableByDistance(1,4534);
        assertEquals(7,o);
        o = switchTable.getNodeForCableByDistance(1,271);
        assertEquals(2,o);
        o = switchTable.getNodeForCableByDistance(1,275);
        assertEquals(1,o);
        o = switchTable.getNodeForCableByDistance(1,271);
        assertEquals(2,o);     
        o = switchTable.getNodeForCableByDistance(1,190);
        assertEquals(5,o);
    }
    
    @Test
    public void testGetNodeDescription() throws Exception {
        System.out.println("testGetNodeForCableByDistance()");
        int o = switchTable.getNodeForCableByDistance(1,4534);
        testsTable.putMeasurement(1, "OK", 2800, 2800);
        testsTable.putMeasurement(1, "OK", 2800, 2800);
        testsTable.putMeasurement(1, "OK", 2800, 2800);
        testsTable.putMeasurement(1, "OK", 2800, 2800);
        switchTable.addNode(1, "11 street");
        assertEquals(switchTable.getNodeDescription(1),"11 street");
        assertEquals(switchTable.getNodeDescription(2),"at station");
        assertEquals(switchTable.getNodeDescription(3),"last metres");
        assertEquals(switchTable.getNodeDescription(4),"branch");
        assertEquals(switchTable.getNodeDescription(5),"branch2");
        assertEquals(switchTable.getNodeDescription(6),"111 street");
        assertEquals(switchTable.getNodeDescription(7),"11 street");
        
        
    }
    
        
    @Test
    public void testDoesCableHaveNodes() throws Exception {
        System.out.println("testDoesCableHaveNodes()");
        assertTrue(switchTable.doesCableHaveNodes(1));
        switchTable.deleteAllNodesForCable(1);
        assertFalse(switchTable.doesCableHaveNodes(1));
        
    }

}
