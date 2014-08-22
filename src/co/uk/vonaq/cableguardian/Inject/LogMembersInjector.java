package co.uk.vonaq.cableguardian.Inject;

import co.uk.vonaq.cableguardian.utils.LogFormatter;
import com.google.inject.MembersInjector;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogMembersInjector<T> implements MembersInjector<T> {

    private final Field field;
    private final Logger logger;
    private static FileHandler fh;

    public LogMembersInjector(Field field) {
        this.field = field;
        this.logger = Logger.getLogger(field.getDeclaringClass().getName());
        try {
            if (fh == null) {
                fh = new FileHandler("/var/log/cable_guardian/log.txt");
            }
            fh.setFormatter(new LogFormatter());
            logger.addHandler(fh);

        } catch (IOException | SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        logger.setLevel(Level.ALL);
        field.setAccessible(true);
    }

    public void injectMembers(T t) {
        try {
            field.set(t, logger);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
