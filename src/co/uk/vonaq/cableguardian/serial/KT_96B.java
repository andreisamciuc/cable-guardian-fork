package co.uk.vonaq.cableguardian.serial;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.core.Constants;
import co.uk.vonaq.cableguardian.exceptions.BoardDataException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class KT_96B implements Board {

    @Log
    private Logger logger;
    private FileInputStream inputStream;
    private FileOutputStream outputStream;

    @Inject
    public KT_96B(Constants constants) throws Exception {
        String file = constants.properties.getProperty("uartNodeFile");
        inputStream = new FileInputStream(file);
        outputStream = new FileOutputStream(file);
    }

    @Override
    public int getLenth() throws IOException, BoardDataException {
        sendreadRequest();
        try{Thread.sleep(1000L);}catch(Exception e){}
        return parse(getBytes());
    }

    private int parse(int[] arg) {
        int value = 0;
        try {
            value = arg[1] * 256;
            value += arg[2];
            if (arg[2] == 0x02) {
                value = value * (-1);
            }
            int d1 = (arg[3] & 0b11110000) / 16;
            int radix = arg[3] & 0b1111;
            switch (radix) {
                case 0:
                    value *= 1000;
                    break;
                case 1:
                    value *= 100;
                    break;
                case 2:
                    value *= 10;
                    break;
                case 3:
                    value *= 1;
                    break;
            }
//            int contacts = (arg[4] & 0b11110000) / 16;
//            int unit = (arg[4] & 0b1111);
        } catch (ArrayIndexOutOfBoundsException e1) {
        }
//        System.out.println("parsed "+value);
        return value;
    }

    @Override
    public void sendreadRequest() throws IOException {
        outputStream.write("SqE".getBytes());
    }

    private int[] getBytes() throws IOException, BoardDataException {
        int byteRead;
        int timeout = 50;
        int byteSkipped = 0;
        long lastTime = System.currentTimeMillis();
        while ((byteRead = inputStream.read()) > -1) {
            //this one clears the buffer.
            byteSkipped++;
            long now = System.currentTimeMillis();
            if ((now - lastTime > timeout) && byteRead == 83) {
                break;
            }
        }
        start:
        while (true) {
            //this one reads untill start bit.
            if ((byteRead = inputStream.read()) > 0) {
                byteSkipped++;
                if (byteRead == 83) {
                    break start;
                }
            }
        }
        int[] text = new int[5];
        for (int i = 0; i < 5; i++) {
            text[i] = (int) inputStream.read();
        }
        if (text[4] == 69) {
            throw new BoardDataException();
        }
//        System.out.println("bytes Skipped = "+byteSkipped+" output is text ");
//        for(int i : text){
//            System.out.print(":"+i);
//        }
        return text;
        
    }
}
