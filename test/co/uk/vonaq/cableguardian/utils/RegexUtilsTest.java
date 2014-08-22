package co.uk.vonaq.cableguardian.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class RegexUtilsTest {
    @Test
    public void testAreNumbersSame() throws Exception {
        assertTrue(RegexUtils.areNumbersSame("8787874444","4523574444"));
        assertTrue(RegexUtils.areNumbersSame("8787874433","4523574433"));
        assertFalse(RegexUtils.areNumbersSame("8787874446","4523574444"));
        assertFalse(RegexUtils.areNumbersSame("8787884444","4523574444"));
    }
    @Test
    public void testvalidateEmail() throws Exception {
        assertTrue(RegexUtils.validEmail("goba@boba.com"));
        assertTrue(RegexUtils.validEmail("g3543545ob35235235a@bo35235b35a.cu"));
        assertFalse(RegexUtils.validEmail("53252345"));
        assertFalse(RegexUtils.validEmail("gobfff    a@boba.com"));
        assertFalse(RegexUtils.validEmail(""));
        
    }
    
    @Test(expected = NullPointerException.class)
    public void testvalidateEmail2() throws Exception {
        assertTrue(RegexUtils.validEmail(null));
        assertTrue(RegexUtils.validEmail(null));
        assertFalse(RegexUtils.validEmail(""));
    }
}
