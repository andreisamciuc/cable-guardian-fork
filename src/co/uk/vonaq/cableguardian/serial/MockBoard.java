package co.uk.vonaq.cableguardian.serial;

import java.io.IOException;
import java.util.Random;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MockBoard implements Board {

    private final int MAX = 950;
    private final int MIN = 710;
    @Inject
    private Random r;

    public MockBoard() throws Exception {
        r = new Random();
    }

    public int getLenth() throws IOException {
        try{Thread.sleep(1000);}catch(Exception e){}
        return r.nextInt(MAX - MIN + 1) + MIN;
    }

    @Override
    public void sendreadRequest() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
