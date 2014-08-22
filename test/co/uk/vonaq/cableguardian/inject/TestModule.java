package co.uk.vonaq.cableguardian.inject;

import co.uk.vonaq.cableguardian.Inject.LogTypeListener;
import co.uk.vonaq.cableguardian.core.Constants;
import co.uk.vonaq.cableguardian.core.development.FakeGpioControl;
import co.uk.vonaq.cableguardian.db.DBConnector;
import co.uk.vonaq.cableguardian.db.DBConnectorMysql;
import co.uk.vonaq.cableguardian.gpio.GPIOControl;
import co.uk.vonaq.cableguardian.serial.Board;
import co.uk.vonaq.cableguardian.serial.MockBoard;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import javax.inject.Singleton;

public class TestModule extends AbstractModule {    
    @Override
    protected void configure() {
        Constants constants = new Constants();
        bindListener(Matchers.any(), new LogTypeListener());
        bind(Board.class).to(MockBoard.class);
        bind(GPIOControl.class).to(FakeGpioControl.class).in(Singleton.class);
        //bind(Board.class).to(MockBoard.class).in(Singleton.class);
        //bind(DBConnector.class).to(DBConnectorMysql.class).in(Singleton.class);
        bind(DBConnector.class).to(DBConnectorMysql.class);
        //bind(MessageSender.class).to(MessageSenderforTests.class);
        bind(String.class).annotatedWith(Names.named("JDBC_URL")).toInstance("jdbc:mysql://"+constants.properties.getProperty("jdbc_url")+"?");
        bind(String.class).annotatedWith(Names.named("JDBC_CRED")).toInstance("user="+constants.properties.getProperty("jdbc_cred_user")+"&password="+constants.properties.getProperty("jdbc_cred_password"));
    }

}