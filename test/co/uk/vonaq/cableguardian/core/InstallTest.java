package co.uk.vonaq.cableguardian.core;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InstallTest {
    
    public InstallTest() {
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

    //this should be implemented.
    @Test(timeout = 100)
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        //Install.main(args);
        assertEquals(new Integer(1), new Integer(1));
        assertNull(null);
        assertNotNull(1);
        assertArrayEquals(new int[]{1,2,3},new int[]{1,2,3});
        assertTrue(true);
        assertFalse(false);
        assertSame(this,this); 
        assertNotSame(this, org.junit.Test.class);
        assertThat(123, is(123));
        assertThat(123, not( is(345) ) );
        assertThat(123,instanceOf(Integer.class));
    }
    
}
