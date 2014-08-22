package co.uk.vonaq.cableguardian.utils;

import co.uk.vonaq.cableguardian.Inject.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileCopier {

    @Log
    private Logger logger;

    public void copyFile(String sourcePathInJar, String destinationInFileSystem, String fileName) {
        InputStream stream = FileCopier.class.getResourceAsStream(sourcePathInJar + fileName);
        //note that each / is a directory down in the "jar tree" been the jar the root of the tree"
        if (stream == null) {
            logger.log(Level.SEVERE, "File copy error: " + sourcePathInJar + " is not found. ");
        } else {
            OutputStream resStreamOut = null;
            int readBytes;
            byte[] buffer = new byte[4096];
            try {
                resStreamOut = new FileOutputStream(new File(destinationInFileSystem + fileName));
                while ((readBytes = stream.read(buffer)) > 0) {
                    resStreamOut.write(buffer, 0, readBytes);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "", e);
            } finally {
                try {
                    stream.close();
                    resStreamOut.close();
                } catch (Exception e1) {
                }
            }
        }
    }
}
