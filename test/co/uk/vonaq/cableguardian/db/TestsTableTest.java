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
public class TestsTableTest {
    @Inject
    DBConnector dc;
    @Inject
    AlarmsTable alarmsTable;
    @Inject
    TestsTable testsTable;
    @Inject
    DBStructureCreator dbc;

    @Inject
    public TestsTableTest() throws IOException, Exception {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException, Exception {
        dbc.createDBStructure();
        System.out.println("testSetup");
        System.out.println("putMeasurement");
        int result;
        result = testsTable.putMeasurement(3, "OK", 280, 280);
        assertEquals(1, result);
        result = testsTable.putMeasurement(3, "OK", 280, 280);
        assertEquals(2, result);
        result = testsTable.putMeasurement(3, "OK", 280, 200);
        assertEquals(3, result);
        result = testsTable.putMeasurement(3, "OK", 280, 227);
        assertEquals(4, result);
        result = testsTable.putMeasurement(3, "Fail Continue", 0, 280);
        assertEquals(5, result);
        result = testsTable.putMeasurement(3, "fail tolerance exceeded difference:128", 278, 150);
        assertEquals(6, result);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of putMeasurement method, of class TestsTable.
     */
    @Test
    public void testPutMeasurement() throws Exception {

    }

    /**
     * Test of getCableAverage method, of class TestsTable.
     */
    @Test
    public void testGetAverageCable() throws Exception {
        System.out.println("getAverageCable");
        assertEquals(253, testsTable.getCableAverage(3));
    }

    /**
     * Test of getAverageCableUntilGood method, of class TestsTable.
     */
    @Test
    public void testGetAverageCableUntilGood() throws Exception {
        System.out.println("getAverageCableUntilGood");
        assertEquals(246, testsTable.getAverageCableUntilGood(3));
    }

    /**
     * Test of deleteAllTestForCable method, of class TestsTable.
     */
    @Test
    public void testDeleteAllTestForCable() throws Exception {
        System.out.println("deleteAllTestForCable");
        testsTable.deleteAllTestForCable(3);
        assertEquals(0, testsTable.getAverageCableUntilGood(3));
    }

}
