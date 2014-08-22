package co.uk.vonaq.cableguardian.utils;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileCopierTest {
    
    public FileCopierTest() {
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
     * Test of copyFile method, of class FileCopier.
     */
    @Test
    public void testCopyFile() {
        System.out.println("copyFile");
        new FileCopier().copyFile("/", "/tmp/", "cable_guardian.conf");
        assertTrue(new File("/tmp/cable_guardian.conf").exists());
    }
    
}
