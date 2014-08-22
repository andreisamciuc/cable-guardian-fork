package co.uk.vonaq.cableguardian.core;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.db.SettingsTable;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

//this class should be run once after Jar was deployed
//should create sql tructure, ...
//todo copy logrotate and upstart scripts.
public class SetIp {

    static {
        File f = new File("/var/log/cable_guardian/");
        f.mkdir();
    }
    @Log
    private Logger logger;
    @Inject
    private SettingsTable settingsTable;

    public void setIp() throws IOException, InterruptedException, SQLException {
        logger.log(Level.INFO, "Setting the ip");
        String ip = settingsTable.getSetting("ip_address");
        String mask = settingsTable.getSetting("ip_netmask");
        String gate = settingsTable.getSetting("ip_gateway");
        String dns = settingsTable.getSetting("ip_dns");
        String cmd = "/var/www/setip.sh " + ip + " " + mask + " " + gate + " " + dns;
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);
        pr.waitFor();
    }
}
