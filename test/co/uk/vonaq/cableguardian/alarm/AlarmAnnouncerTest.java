package co.uk.vonaq.cableguardian.alarm;

import co.uk.vonaq.cableguardian.core.Constants;
import co.uk.vonaq.cableguardian.db.*;
import co.uk.vonaq.cableguardian.inject.GuiceIntegration;
import co.uk.vonaq.cableguardian.utils.PropertiesUtils;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.inject.Inject;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceIntegration.class)
public class AlarmAnnouncerTest {
    @Inject DBStructureCreator dbc;
    @Inject AlarmAnnouncer alarmAnnouncer;
    @Inject TestsTable testsTable;
    @Inject AlarmsTable alarmsTable;
    @Inject CableTable cablesTable;
    @Inject AdminsTable adminsTable;
    @Inject Constants constants;
    @Inject DBConnector dBConnector;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        dbc.createDBStructure();
        Properties props = PropertiesUtils.load();
        constants.properties = props;
        ResourceBundle strings = PropertiesUtils.loadLanguage(props);
        constants.phrases = strings;
        System.out.println("resourses loaded;");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("RegexUtilsTest tearDown(). And Says Bye!");
    }

    @Test
    //@SuppressWarnings("empty-statement")
    public void testAnnounce() throws Exception {
        System.out.println("testAnnounce() is running2");
        int cableNumber = 1;
        adminsTable.configureAdmin(1,"Idiot","073453454","pyz@fu.bo","secr4t");
        cablesTable.configureCable(cableNumber, 1, 10, "Enabled", "Enabled", "SomeLine","Cable");
        //try{Thread.sleep(1000L);}catch(InterruptedException e){};
        int newKey = testsTable.putMeasurement(cableNumber, "fail tolerance exceeded difference:86", 3214, 1029);
        System.out.println("newKey = "+newKey);
        newKey = alarmsTable.putAlarm("New", cableNumber, newKey,"",0);
        System.out.println("newKey = "+newKey);
        alarmAnnouncer.announce(newKey);
    }

    /**
     * Test of announceFailedSms method, of class AlarmAnnouncer.
     */
    @Test @Ignore
    public void testAnnounceFailedSms() throws Exception {
        System.out.println("announceFailedSms");
        alarmsTable.putAlarm("New", 1, 12,"ok",0);
        assertTrue(true);
//        alarmAnnouncer.announceFailedSms(1);
//        String query = "select * from cable_guardian.alarms";
//        ResultSet result = dBConnector.executeQuery(query);
//        assertTrue(result.next());
        //fail("The test case is a prototype.");
    }
}
