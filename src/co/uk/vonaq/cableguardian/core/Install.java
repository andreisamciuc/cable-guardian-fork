package co.uk.vonaq.cableguardian.core;

import co.uk.vonaq.cableguardian.Inject.DefaultModule;
import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.db.DBStructureCreator;
import co.uk.vonaq.cableguardian.utils.FileCopier;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

//this class should be run once after Jar was deployed
//should create sql tructure, ...
//todo copy logrotate.
public class Install {

    static {
        File f = new File("/var/log/cable_guardian/");
        f.mkdir();
    }
    @Log
    private Logger logger;
    @Inject
    private FileCopier fc;
    
    public Install() throws InterruptedException{
        Thread.sleep(1L);
    }

    public void install() throws IOException {
        logger.log(Level.INFO, "Install Script start");
        Injector injector = Guice.createInjector(Stage.PRODUCTION, new DefaultModule());
        DBStructureCreator dbc = injector.getInstance(DBStructureCreator.class);
        dbc.createDBStructure();
        fc.copyFile("/", "/etc/init/", "cable_guardian.conf");
        fc.copyFile("/", "/etc/init/", "cable_guardian_smsd.conf");
        fc.copyFile("/", "/etc/logrotate.d/", "cable_guardian");
        fc.copyFile("/", "/var/www/", "setip.sh");
        fc.copyFile("/", "/etc/cron.hourly/", "cable_guardian_hourly");
        new File("/etc/cron.hourly/cable_guardian_hourly").setExecutable(true);
        File script = new File("/var/www/setip.sh");
        script.setExecutable(true);
        boolean success = (new File("/home/ubuntu/.config/gnokii/")).mkdirs();
        if (!success) {
            fc.copyFile("/", "/home/ubuntu/.config/gnokii/", "config");
        }
        logger.log(Level.INFO, "Install Script done");
    }
}
