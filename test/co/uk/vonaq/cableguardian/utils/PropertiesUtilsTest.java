package co.uk.vonaq.cableguardian.utils;

import java.util.Properties;
import java.util.ResourceBundle;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PropertiesUtilsTest {
    
    public PropertiesUtilsTest() {
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
     * Test of class PropertiesUtils.
     */
    @Test
    public void test() throws Exception {
        System.out.println("teting PropertiesUtils");
        Properties result = PropertiesUtils.load();
        assertNotNull(result);
        ResourceBundle result_ = PropertiesUtils.loadLanguage(result);
        assertNotNull(result_);
    }


    
}
