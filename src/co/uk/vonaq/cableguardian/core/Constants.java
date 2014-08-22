package co.uk.vonaq.cableguardian.core;

import co.uk.vonaq.cableguardian.utils.PropertiesUtils;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.inject.Singleton;

@Singleton
public class Constants {

    public Properties properties;
    public ResourceBundle phrases;

    public Constants() {
        try {
            properties = PropertiesUtils.load();
            phrases = PropertiesUtils.loadLanguage(properties);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
