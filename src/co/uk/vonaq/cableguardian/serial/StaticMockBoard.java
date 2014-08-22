package co.uk.vonaq.cableguardian.serial;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class StaticMockBoard implements Board {

    private final int MAX = 950;
    private final int MIN = 710;
    private int cableNumber;
    @Inject
    private HashMap<Integer, Integer> table;
    @Inject
    private Random r;

    @Override
    public int getLenth() throws IOException {
        if (table.containsKey(cableNumber)) {
            return table.get(cableNumber);
        } else {
            int number = r.nextInt(MAX - MIN + 1) + MIN;
            table.put(cableNumber, number);
            return number;
        }
    }

    public void setCable(int cableNumber) {
        this.cableNumber = cableNumber;
    }

    @Override
    public void sendreadRequest() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
