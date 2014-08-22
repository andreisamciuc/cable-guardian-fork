package co.uk.vonaq.cableguardian.core;

import co.uk.vonaq.cableguardian.inject.GuiceIntegration;
import javax.inject.Inject;
import junit.extensions.PA;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceIntegration.class)
public class MainLoopTest {
    
    @Inject MainLoop instance;
    
    public MainLoopTest() {
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

    /**
     * Test of run method, of class MainLoop.
     */
    @Test @Ignore
    //here is good place to start using mochito
    public void testRun() {
        System.out.println("run");
    }
    
    @Test
    public void testCalculateDifference(){
        //this PA stuff is nice when You want to test private methods and access private fields
        //https://code.google.com/p/privilegedaccessor/
        assertEquals(0  ,PA.invokeMethod(instance, "calculateDifference(int,int)", 100, 110));
        assertEquals(100,PA.invokeMethod(instance, "calculateDifference(int,int)", 42, 84));
        assertEquals(300,PA.invokeMethod(instance, "calculateDifference(int,int)", 42, 168));
        assertEquals(5  ,PA.invokeMethod(instance, "calculateDifference(int,int)", 1024, 1076));
        assertEquals(6  ,PA.invokeMethod(instance, "calculateDifference(int,int)", 4200, 3948));
        assertEquals(7  ,PA.invokeMethod(instance, "calculateDifference(int,int)", 5000, 4650));
        assertEquals(0  ,PA.invokeMethod(instance, "calculateDifference(int,int)", 0, 10));
        assertEquals(0  ,PA.invokeMethod(instance, "calculateDifference(int,int)", 0, 1));
        assertEquals(0  ,PA.invokeMethod(instance, "calculateDifference(int,int)", 10, 1));
        assertEquals(0  ,PA.invokeMethod(instance, "calculateDifference(int,int)", 10, 0));       
        assertEquals(0  ,PA.invokeMethod(instance, "calculateDifference(int,int)", 5, 14));
        assertEquals(0  ,PA.invokeMethod(instance, "calculateDifference(int,int)", 0, 8));     
    }
        
}
