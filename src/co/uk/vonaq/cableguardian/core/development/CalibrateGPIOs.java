package co.uk.vonaq.cableguardian.core.development;

import co.uk.vonaq.cableguardian.Inject.DefaultModule;
import co.uk.vonaq.cableguardian.multiplexer.RelayControl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import java.io.IOException;

public class CalibrateGPIOs {

    public static void main(String... arg) throws InterruptedException, IOException {
        System.out.println("Hi From Calibrate");
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new DefaultModule());//todo when deploying change to Stage.PRODUCTION!!!
        RelayControl rc = injector.getInstance(RelayControl.class);
        int i = 2;
        rc.enable();
        while (true) {
            System.out.println("Set cable to " + i);
            rc.setCable(i);
            i++;
            //Thread.sleep(10000L);
            System.exit(0);
        }
    }
}
