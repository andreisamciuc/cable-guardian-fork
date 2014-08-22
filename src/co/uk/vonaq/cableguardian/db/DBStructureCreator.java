package co.uk.vonaq.cableguardian.db;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.utils.StreamConverter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBStructureCreator {

    @Log
    private Logger logger;

    public void createDBStructure() throws IOException {
        String query = getStructure();
        String cmd = "mysql -uroot -ppassw0rd";
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);
        BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()), 1024);
        buf.write(query);
        buf.write(";\n\n\n");
        buf.write("exit\n\n\n");
        buf.flush();
        buf.close();
        try {
            pr.waitFor();
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
    private String getStructure() throws IOException {
        InputStream stream = DBStructureCreator.class.getClassLoader().getResourceAsStream("sql_structure");
        if (stream == null) {
            logger.log(Level.INFO, "File read error: sql_structure is not found.");
        }
        return StreamConverter.convertStreamToString(stream);
    }
}
