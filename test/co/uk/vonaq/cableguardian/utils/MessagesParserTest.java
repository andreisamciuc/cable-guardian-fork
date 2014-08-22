package co.uk.vonaq.cableguardian.utils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessagesParserTest {
    
    public MessagesParserTest() {
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
    public void testParseFromField(){
        System.out.println("testParseFromField()");
        assertEquals("email@email.com", MessagesParser.parseFromField("sjhfsdb sdfsdf <email@email.com>"));
        assertEquals("email7@email.com", MessagesParser.parseFromField("sjhfsdbsdfsdf <email7@email.com>"));
        assertEquals("", MessagesParser.parseFromField("sjhfsdb sdfsdf <>"));
        assertEquals("email@email", MessagesParser.parseFromField("sjhfsdb sdfsdf <email@email>"));
        assertEquals("dafuq@vronuq.com", MessagesParser.parseFromField("sjhfsdb sdfsdf xxcvxc <dafuq@vronuq.com>"));
    }

    /**
     * Test of parse method, of class MessagesParser.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        String message = "";
        Tuple<MessageAction, Integer, String> expResult = null;
        Tuple<MessageAction, Integer, String> result = MessagesParser.parse(message);
        assertEquals(expResult, result);
                
        System.out.println("testParse() is running2");
        
        assertEquals(new Tuple<>(MessageAction.MINUS, 45, null).first, MessagesParser.parse("- 45").first);
        assertEquals(new Tuple<>(MessageAction.MINUS, 45, null).second, MessagesParser.parse("- 45").second);
        assertEquals(new Tuple<>(MessageAction.MINUS, 45, null).third, MessagesParser.parse("- 45").third);
        
        assertEquals(new Tuple<>(MessageAction.PLUS, 1, null).first, MessagesParser.parse("+  1").first);
        assertEquals(new Tuple<>(MessageAction.PLUS, 1, null).second, MessagesParser.parse("+  1").second);
        assertEquals(new Tuple<>(MessageAction.PLUS, 1, null).third, MessagesParser.parse("+  1").third);
        
        assertEquals(new Tuple<>(MessageAction.STA, 1, null).first, MessagesParser.parse("sta  1").first);
        assertEquals(new Tuple<>(MessageAction.STA, 1, null).second, MessagesParser.parse("sta  1").second);
        assertEquals(new Tuple<>(MessageAction.STA, 1, null).third, MessagesParser.parse("sta  1").third);
        
        assertEquals(new Tuple<>(MessageAction.CLR, 1, null).first, MessagesParser.parse("clr  1").first);
        assertEquals(new Tuple<>(MessageAction.CLR, 1, null).second, MessagesParser.parse("clr  1").second);
        assertEquals(new Tuple<>(MessageAction.CLR, 1, null).third, MessagesParser.parse("clr  1").third);
        
        assertEquals(new Tuple<>(MessageAction.ACK, 1, null).first, MessagesParser.parse("ack  1").first);
        assertEquals(new Tuple<>(MessageAction.ACK, 1, null).second, MessagesParser.parse("ack  1").second);
        assertEquals(new Tuple<>(MessageAction.ACK, 1, null).third, MessagesParser.parse("ack  1").third);
        
        assertEquals(new Tuple<>(MessageAction.ADD, 1, "very nice cable").first,  MessagesParser.parse("addnode 1 very nice cable").first);
        assertEquals(new Tuple<>(MessageAction.ADD, 1, "very nice cable").second, MessagesParser.parse("AdDnODE 1 very nice cable").second);
        assertEquals(new Tuple<>(MessageAction.ADD, 1, "very nice cable").third,  MessagesParser.parse("AdDnODE 1 very nice cable").third);
        
        assertEquals(new Tuple<>(MessageAction.ADD, 1, "very nice cable").first,  MessagesParser.parse("addnode  1  very nice cable").first);
        assertEquals(new Tuple<>(MessageAction.ADD, 1, "very nice cable").second, MessagesParser.parse("AdDnODE  1  very nice cable").second);
        assertEquals(new Tuple<>(MessageAction.ADD, 1, "very nice cable").third,  MessagesParser.parse("AdDnoDE  1  very nice cable").third);
        
        assertNotSame(new Tuple<>(MessageAction.ACK, 45, null).first, MessagesParser.parse("- 45").first);
        assertNotSame(new Tuple<>(MessageAction.MINUS, 43, null).second, MessagesParser.parse("- 45").second);
        
        assertNotSame(new Tuple<>(MessageAction.CLR, 1, null).first, MessagesParser.parse("+  1").first);
        assertNotSame(new Tuple<>(MessageAction.PLUS, 14, null).second, MessagesParser.parse("+  1").second);
        
        assertNotSame(new Tuple<>(MessageAction.MINUS, 1, null).first, MessagesParser.parse("sta  1").first);
        assertNotSame(new Tuple<>(MessageAction.STA, 1, null).second, MessagesParser.parse("sta  15").second);
        
        assertNotSame(new Tuple<>(MessageAction.MINUS, 1, null).first, MessagesParser.parse("clr  1").first);
        assertNotSame(new Tuple<>(MessageAction.CLR, 1, null).second, MessagesParser.parse("clr  12").second);
        
        assertNotSame(new Tuple<>(MessageAction.PLUS, 1, null).first, MessagesParser.parse("ack  1").first);
        assertNotSame(new Tuple<>(MessageAction.ACK, 1, null).second, MessagesParser.parse("ack  77").second);
        
        assertNull(MessagesParser.parse("1ack  1"));
        assertNull(MessagesParser.parse("ack77"));
        assertNull(MessagesParser.parse("ack  sta"));
        assertNull(MessagesParser.parse("77 sta"));
        assertNull(MessagesParser.parse("Fuck off"));
    }
    
}
