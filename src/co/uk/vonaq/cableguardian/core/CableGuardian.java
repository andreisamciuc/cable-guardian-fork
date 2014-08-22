package co.uk.vonaq.cableguardian.core;

import co.uk.vonaq.cableguardian.Inject.DefaultModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

public class CableGuardian {

    public static void main(String[] args) throws Exception {
        System.out.println("CableGuardian Says Hi");
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new DefaultModule());//todo when deploying change to Stage.PRODUCTION!!!
//        args = new String[1];
//        args[0] = "install";
        if (args.length != 0 && args[0].equalsIgnoreCase("install")) {
            System.out.println("Installing...");
            injector.getInstance(Install.class).install();
        } else {
            injector.getInstance(SetIp.class).setIp();
            injector.getInstance(SetTime.class).start();
            injector.getInstance(MainLoop.class).start();
            injector.getInstance(MessageThread.class).start();
            injector.getInstance(DeletetionThread.class).start();
        }
    }
}
