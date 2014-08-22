package co.uk.vonaq.cableguardian.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesUtils {

    private static final String propertiesFileName = "main.properties";

    //todo make file load not from jar, but from disk. If available.
    public static Properties load() throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        Class d = PropertiesUtils.class;

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFileName);
        properties.load(in);
        in.close();
        return properties;

//        create();//just in case its not present.
//        Properties properties = new Properties();
//        File file = new File(propertiesFileName);
//        FileInputStream fis = new FileInputStream(propertiesFileName);
//        
//        System.out.println("4444444"+file.getAbsolutePath());
//        properties.load(fis);
//        fis.close();
//        return properties;
    }

    private static void create() throws FileNotFoundException, IOException {//creates properties file if it does not exists.
        File propsFile = new File(propertiesFileName);
        if (!propsFile.exists()) {
            propsFile.createNewFile();
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(propsFile);
            properties.load(fis);
            fis.close();
            properties.setProperty("language", "en");
            properties.setProperty("country", "uk");
            properties.setProperty("uartNodeFile", "/dev/ttyO1");
            FileOutputStream fis2 = new FileOutputStream(propertiesFileName);
            properties.store(fis2, null);
            fis2.close();
        }
    }

    public static ResourceBundle loadLanguage(Properties properties) {
        String language = properties.getProperty("language");
        String country = properties.getProperty("country");
        Locale currentLocale = new Locale(language, country);
        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
        return messages;
    }
}
